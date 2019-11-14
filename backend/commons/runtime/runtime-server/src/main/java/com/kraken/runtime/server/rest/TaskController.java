package com.kraken.runtime.server.rest;

import com.google.common.base.Preconditions;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.service.ResultUpdater;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.tools.sse.SSEService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/task")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class TaskController {

  @NonNull ResultUpdater resultUpdater;
  @NonNull TaskService taskService;
  @NonNull TaskListService taskListService;
  @NonNull SSEService sse;

  @PostMapping("/{type}/{replicas}")
  public Mono<String> run(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                          @PathVariable("type") final TaskType type,
                          @PathVariable("replicas") final Integer replicas,
                          @RequestBody() final Map<String, String> environment) {
    log.info(String.format("Run task %s", type));
    return taskService.hostsCount()
        .map(hostsCount -> {
          Preconditions.checkArgument(hostsCount >= replicas, String.format("Insufficient capacity (%d) to run %d replicas!", hostsCount, replicas));
          return hostsCount;
        })
        .then(taskService.execute(applicationId, type, replicas, environment))
        .flatMap(taskId -> resultUpdater.taskExecuted(taskId, type, environment));
  }

  @PostMapping("/cancel/{type}")
  public Mono<String> cancel(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestParam("taskId") final String taskId,
                             @PathVariable("type") final TaskType type) {
    log.info(String.format("Cancel task %s", taskId));
    return taskService.cancel(applicationId, taskId, type).flatMap(aVoid -> resultUpdater.taskCanceled(taskId));
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<List<Task>>> watch() {
    log.info("Watch tasks lists");
    return sse.keepAlive(taskListService.watch());
  }

  @GetMapping(value = "/list")
  public Flux<Task> list() {
    return taskListService.list();
  }
}
