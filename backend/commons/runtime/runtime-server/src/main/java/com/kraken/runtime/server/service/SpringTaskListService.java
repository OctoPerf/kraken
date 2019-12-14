package com.kraken.runtime.server.service;

import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.Task;
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
public class SpringTaskListService implements TaskListService {

  final static Duration WATCH_TASKS_DELAY = Duration.ofSeconds(2);

  @NonNull FlatContainersToTask toTask;
  @NonNull TaskService taskService;

  @Override
  public Flux<Task> list(final Optional<String> applicationId) {
    return taskService.list(applicationId).groupBy(FlatContainer::getTaskId).flatMap(toTask);
  }

  @Override
  public Flux<List<Task>> watch(final Optional<String> applicationId) {
    return Flux.interval(WATCH_TASKS_DELAY)
        .flatMap(aLong -> this.list(applicationId).collectList())
        .distinctUntilChanged();
  }
}
