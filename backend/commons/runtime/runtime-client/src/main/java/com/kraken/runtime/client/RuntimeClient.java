package com.kraken.runtime.client;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

public interface RuntimeClient {

  Mono<Task> waitForPredicate(Predicate<Task> predicate);

  Mono<Task> waitForStatus(String taskId, ContainerStatus status);

  Mono<Container> setStatus(String taskId, String hostId, String containerId, ContainerStatus status);

}
