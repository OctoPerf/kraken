package com.kraken.runtime.server.rest;

import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.event.TaskCancelledEvent;
import com.kraken.runtime.event.TaskExecutedEvent;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.tools.event.bus.EventBus;
import com.kraken.tools.sse.SSEService;
import com.runtime.context.api.ExecutionContextService;
import com.runtime.context.entity.CancelContext;
import com.runtime.context.entity.ExecutionContext;
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

import static java.util.Optional.of;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController()
@RequestMapping("/task")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
public class TaskController {

  @NonNull EventBus eventBus;
  @NonNull TaskService taskService;
  @NonNull TaskListService taskListService;
  @NonNull ExecutionContextService executionContextService;
  @NonNull SSEService sse;

  @PostMapping(produces = TEXT_PLAIN_VALUE)
  public Mono<String> run(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                          @RequestBody() final ExecutionEnvironment environment) {
    log.info(String.format("Execute %s task", environment.getTaskType()));
    return executionContextService.newExecuteContext(applicationId, environment)
        .flatMap(taskService::execute)
        .doOnNext(_context -> eventBus.publish(TaskExecutedEvent.builder().context(_context).build()))
        .map(ExecutionContext::getTaskId);
  }

  @DeleteMapping(value = "/cancel/{type}", produces = TEXT_PLAIN_VALUE)
  public Mono<String> cancel(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestParam("taskId") final String taskId,
                             @PathVariable("type") final String type) {
    log.info(String.format("Cancel task %s", taskId));
    return executionContextService.newCancelContext(applicationId, taskId, type)
        .flatMap(taskService::cancel)
        .doOnNext(context -> eventBus.publish(TaskCancelledEvent.builder().context(context).build()))
        .map(CancelContext::getTaskId);
  }

  @DeleteMapping(value = "/remove/{type}", produces = TEXT_PLAIN_VALUE)
  public Mono<String> remove(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestParam("taskId") final String taskId,
                             @PathVariable("type") final String type) {
    log.info(String.format("Remove task %s", taskId));
    return executionContextService.newCancelContext(applicationId, taskId, type)
        .flatMap(taskService::remove)
        .map(CancelContext::getTaskId);
  }

  @GetMapping(value = "/watch/{applicationId}")
  public Flux<ServerSentEvent<List<Task>>> watch(@PathVariable("applicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    log.info("Watch tasks lists");
    return sse.keepAlive(taskListService.watch(of(applicationId)));
  }

  @GetMapping(value = "/list")
  public Flux<Task> list(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId) {
    return taskListService.list(of(applicationId));
  }
}
