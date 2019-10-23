package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.service.ResultUpdater;
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
  @NonNull TaskService service;
  @NonNull SSEService sse;

  @PostMapping("/{type}")
  public Mono<String> run(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                          @PathVariable("type") final TaskType type,
                          @RequestBody() final Map<String, String> environment) {
    log.info(String.format("Run task %s", type));
    return service.execute(applicationId, type, environment).flatMap(taskId -> resultUpdater.taskExecuted(taskId, type, environment));
  }

  @PostMapping("/cancel")
  public Mono<String> cancel(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestBody() final Task task) {
    log.info(String.format("Cancel task %s", task.getId()));
    return service.cancel(applicationId, task).flatMap(aVoid -> resultUpdater.taskCanceled(task.getId()));
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<List<Task>>> watch() {
    log.info("Watch tasks lists");
    return sse.keepAlive(service.watch());
  }

  @GetMapping(value = "/list")
  public Flux<Task> list() {
    return service.list();
  }
}
