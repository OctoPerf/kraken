package com.kraken.runtime.api;

import com.kraken.runtime.entity.*;
import reactor.core.publisher.Mono;

public interface ContainerService {

  Mono<Void> attachLogs(String applicationId, String taskId, String containerId);

  Mono<Void> detachLogs(String taskId, String containerId);

  Mono<Container> setStatus(String taskId, String containerId, ContainerStatus status);

}
