package com.kraken.runtime.client;

import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.Task;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

public interface RuntimeClient {

  Mono<Task> waitForPredicate(FlatContainer container, Predicate<Task> predicate);

  Mono<Task> waitForStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setFailedStatus(FlatContainer container);

  Mono<FlatContainer> find(String taskId, String containerName);

}
