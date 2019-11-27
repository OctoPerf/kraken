package com.kraken.runtime.server.rest;

import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.server.service.ResultUpdater;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.tools.sse.SSEService;
import com.kraken.tools.unique.id.IdGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Pattern;
import java.util.List;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

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
  @NonNull IdGenerator idGenerator;

  @PostMapping(produces = TEXT_PLAIN_VALUE)
  public Mono<String> run(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                          @RequestBody() final ExecutionContext context) {
    log.info(String.format("Execute %s task", context.getTaskType()));
    return taskService.execute(context.withApplicationId(applicationId).withTaskId(idGenerator.generate()))
        .flatMap(resultUpdater::taskExecuted);
  }

  @DeleteMapping(value = "/cancel/{type}", produces = TEXT_PLAIN_VALUE)
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
