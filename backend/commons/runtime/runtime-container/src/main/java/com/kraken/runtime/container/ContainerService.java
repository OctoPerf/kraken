package com.kraken.runtime.container;

import com.kraken.runtime.entity.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ContainerService {

  Mono<String> execute(String applicationId,
                       String description,
                       TaskType taskType,
                       Map<String, String> environment);

  Mono<Void> cancel(String applicationId,
                    Task task);

  Flux<Task> listTasks();

  Flux<List<Task>> watchTasks();

  Flux<Log> logs(String applicationId);

  Mono<Void> attachLogs(String applicationId, Container container);

  Mono<Void> detachLogs(Container container);

  Mono<Container> setStatus(String containerId, ContainerStatus status);

}
