package com.kraken.runtime.client;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

public interface RuntimeClient {

  Mono<Task> waitForPredicate(FlatContainer container, Predicate<Task> predicate);

  Mono<Task> waitForStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setFailedStatus(FlatContainer container);

  Mono<FlatContainer> find(String taskId, String containerName);

  Flux<Log> watchLogs(String applicationId);

  Flux<List<Task>> watchTasks(String applicationId);

}
