package com.octoperf.kraken.runtime.client.spring;

import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
final class SpringRuntimeClient implements RuntimeClient {

  @NonNull Owner owner;
  @NonNull TaskLogsService logsService;
  @NonNull TaskListService taskListService;

  @Override
  public Flux<Log> watchLogs() {
    return logsService.listen(owner);
  }

  @Override
  public Flux<List<Task>> watchTasks() {
    return taskListService.watch(owner);
  }

  @Override
  public Mono<Task> waitForPredicate(FlatContainer container, Predicate<Task> predicate) {
    return Mono.error(new UnsupportedOperationException());
  }

  @Override
  public Mono<Task> waitForStatus(FlatContainer container, ContainerStatus status) {
    return Mono.error(new UnsupportedOperationException());
  }

  @Override
  public Mono<Void> setStatus(FlatContainer container, ContainerStatus status) {
    return Mono.error(new UnsupportedOperationException());
  }

  @Override
  public Mono<FlatContainer> find(String taskId, String containerName) {
    return Mono.error(new UnsupportedOperationException());
  }
}
