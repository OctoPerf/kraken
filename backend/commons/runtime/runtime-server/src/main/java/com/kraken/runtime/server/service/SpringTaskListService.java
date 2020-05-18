package com.kraken.runtime.server.service;

import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.Task;
import com.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class SpringTaskListService implements TaskListService {

  final static Duration WATCH_TASKS_DELAY = Duration.ofSeconds(2);

  @NonNull FlatContainersToTask toTask;
  @NonNull TaskService taskService;

  @Override
  public Flux<Task> list(final Owner owner) {
    return taskService.list(owner).groupBy(FlatContainer::getTaskId).flatMap(toTask);
  }

  @Override
  public Flux<List<Task>> watch(final Owner owner) {
    return Flux.interval(WATCH_TASKS_DELAY)
        .flatMap(aLong -> this.list(owner).collectList())
        .distinctUntilChanged();
  }
}
