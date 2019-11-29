package com.kraken.runtime.api;

import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import reactor.core.publisher.Mono;

public interface ContainerService {

  default String logsId(final String taskId, final String containerId, final String containerName) {
    return String.format("%s-%s-%s", taskId, containerId, containerName);
  }

  Mono<String> attachLogs(String applicationId, String taskId, String containerId, String containerName);

  Mono<Void> detachLogs(String applicationId, String id);

  Mono<Void> setStatus(String taskId, String containerId, String containerName, ContainerStatus status);

  Mono<FlatContainer> find(String taskId, String containerName);
}
