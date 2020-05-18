package com.kraken.runtime.backend.api;

import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface ContainerService {

  default String logsId(final String taskId, final String containerId, final String containerName) {
    return String.format("%s-%s-%s", taskId, containerId, containerName);
  }

  Mono<String> attachLogs(Owner owner, String taskId, String containerId, String containerName);

  Mono<Void> detachLogs(Owner owner, String id);

  Mono<Void> setStatus(Owner owner, String taskId, String containerId, String containerName, ContainerStatus status);

  Mono<FlatContainer> find(Owner owner, String taskId, String containerName);
}
