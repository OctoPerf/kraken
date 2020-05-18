package com.kraken.runtime.client.api;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.Task;
import com.kraken.security.authentication.client.api.AuthenticatedClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

public interface RuntimeClient extends AuthenticatedClient {

  Mono<Task> waitForPredicate(FlatContainer container, Predicate<Task> predicate);

  Mono<Task> waitForStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setStatus(FlatContainer container, ContainerStatus status);

  Mono<FlatContainer> find(String taskId, String containerName);

  Flux<Log> watchLogs();

  Flux<List<Task>> watchTasks();

}
