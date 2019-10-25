package com.kraken.runtime.api;

import com.kraken.runtime.entity.*;
import reactor.core.publisher.Mono;

public interface ContainerService {

  Mono<Void> attachLogs(String applicationId, Container container);

  Mono<Void> detachLogs(Container container);

  Mono<Container> setStatus(String taskId, String containerId, ContainerStatus status);

}
