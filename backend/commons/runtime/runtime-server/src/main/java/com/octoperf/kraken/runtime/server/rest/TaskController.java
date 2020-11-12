package com.octoperf.kraken.runtime.server.rest;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.backend.api.HostService;
import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.backend.api.TaskService;
import com.octoperf.kraken.runtime.context.api.ExecutionContextService;
import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.octoperf.kraken.runtime.entity.host.Host;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.event.*;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.tools.event.bus.EventBus;
import com.octoperf.kraken.tools.sse.SSEService;
import com.octoperf.kraken.tools.sse.SSEWrapper;
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
  @NonNull HostService hostService;
  @NonNull ExecutionContextService executionContextService;
  @NonNull SSEService sse;
  @NonNull UserProvider userProvider;

  @PostMapping(produces = TEXT_PLAIN_VALUE)
  public Mono<String> run(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                          @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId,
                          @RequestBody() final ExecutionEnvironment environment) {
    log.info(String.format("Execute %s task", environment.getTaskType().toString()));
    return userProvider.getOwner(applicationId, projectId)
        .flatMap(owner ->
            hostService.list(owner)
                .map(Host::getId)
                .collectList()
                .flatMap(hosts -> {
                  if (environment.getHostIds().stream().anyMatch((String hostId) -> !hosts.contains(hostId))) {
                    return Mono.error(new IllegalArgumentException("Illegal access to hosts."));
                  }
                  return Mono.just(owner);
                }))
        .flatMap(owner -> executionContextService.newExecuteContext(owner, environment))
        .flatMap(taskService::execute)
        .doOnNext(ctx -> eventBus.publish(TaskExecutedEvent.builder().context(ctx).build()))
        .map(ExecutionContext::getTaskId);
  }

  @DeleteMapping(value = "/cancel/{type}", produces = TEXT_PLAIN_VALUE)
  public Mono<String> cancel(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId,
                             @RequestParam("taskId") @Pattern(regexp = "[a-z0-9]*") final String taskId,
                             @PathVariable("type") final TaskType type) {
    log.info(String.format("Cancel task %s", taskId));
    return userProvider.getOwner(applicationId, projectId)
        .flatMap(owner -> executionContextService.newCancelContext(owner, taskId, type))
        .flatMap(taskService::cancel)
        .doOnNext(context -> eventBus.publish(TaskCancelledEvent.builder().context(context).build()))
        .map(CancelContext::getTaskId);
  }

  @DeleteMapping(value = "/remove/{type}", produces = TEXT_PLAIN_VALUE)
  public Mono<String> remove(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                             @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId,
                             @RequestParam("taskId") @Pattern(regexp = "[a-z0-9]*") final String taskId,
                             @PathVariable("type") final TaskType type) {
    log.info(String.format("Remove task %s", taskId));
    return userProvider.getOwner(applicationId, projectId)
        .flatMap(owner -> executionContextService.newCancelContext(owner, taskId, type))
        .flatMap(taskService::remove)
        .map(CancelContext::getTaskId);
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<List<Task>>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                 @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    log.info("Watch tasks lists");
    return userProvider.getOwner(applicationId, projectId).flatMapMany(owner -> sse.keepAlive(taskListService.watch(owner)));
  }

  @GetMapping(value = "/list")
  public Flux<Task> list(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                         @RequestHeader("ProjectId") @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    return userProvider.getOwner(applicationId, projectId).flatMapMany(taskListService::list);
  }

  @GetMapping(value = "/events")
  public Flux<ServerSentEvent<SSEWrapper>> events() {
    log.info("Watch tasks events");
    return sse.keepAlive(sse.merge(ImmutableMap.of(
        "TaskExecutedEvent", eventBus.of(TaskExecutedEvent.class),
        "TaskCreatedEvent", eventBus.of(TaskCreatedEvent.class),
        "TaskStatusUpdatedEvent", eventBus.of(TaskStatusUpdatedEvent.class),
        "TaskCancelledEvent", eventBus.of(TaskCancelledEvent.class),
        "TaskRemovedEvent", eventBus.of(TaskRemovedEvent.class)
    )));
  }
}
