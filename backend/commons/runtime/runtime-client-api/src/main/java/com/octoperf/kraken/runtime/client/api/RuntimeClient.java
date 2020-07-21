package com.octoperf.kraken.runtime.client.api;

import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.Task;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

public interface RuntimeClient extends RuntimeWatchClient {

  Mono<Task> waitForPredicate(FlatContainer container, Predicate<Task> predicate);

  Mono<Task> waitForStatus(FlatContainer container, ContainerStatus status);

  Mono<Void> setStatus(FlatContainer container, ContainerStatus status);

  Mono<FlatContainer> find(String taskId, String containerName);

}
