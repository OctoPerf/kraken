package com.octoperf.kraken.runtime.backend.spring;

import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.backend.api.TaskUpdateHandler;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.event.TaskCreatedEvent;
import com.octoperf.kraken.runtime.event.TaskRemovedEvent;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.event.bus.EventBus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class SpringTaskUpdateHandler implements TaskUpdateHandler {

  @NonNull TaskListService taskListService;
  @NonNull EventBus eventBus;

  @PostConstruct
  public void start() {
    this.scanForUpdates()
        .retryWhen(Retry.backoff(Integer.MAX_VALUE, Duration.ofSeconds(5)))
        .onErrorContinue((throwable, o) -> log.error("Failed to list tasks " + o, throwable))
        .subscribe();
  }

  @Override
  public Flux<List<Task>> scanForUpdates() {
    return taskListService.watch(Owner.PUBLIC)
        .scan((previousTasks, currentTasks) -> {
          final var previousMap = previousTasks.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
          final var currentMap = currentTasks.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
          previousTasks.stream()
              .filter(task -> !currentMap.containsKey(task.getId()))
              .map(task -> TaskRemovedEvent.builder().task(task).build())
              .forEach(eventBus::publish);
          currentTasks.stream()
              .filter(task -> !previousMap.containsKey(task.getId()))
              .map(task -> TaskCreatedEvent.builder().task(task).build())
              .forEach(eventBus::publish);
          currentTasks.stream().filter(task -> previousMap.containsKey(task.getId()))
              .filter(task -> previousMap.get(task.getId()).getStatus() != task.getStatus())
              .map(task -> TaskStatusUpdatedEvent.builder().previousStatus(previousMap.get(task.getId()).getStatus()).task(task).build())
              .forEach(eventBus::publish);
          return currentTasks;
        });
  }
}
