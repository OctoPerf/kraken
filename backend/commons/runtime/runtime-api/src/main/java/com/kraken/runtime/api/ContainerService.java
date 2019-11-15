package com.kraken.runtime.api;

import com.kraken.runtime.entity.*;
import reactor.core.publisher.Mono;

public interface ContainerService {

  default String logsId(final String taskId, final String hostId, final String containerId) {
    return String.format("%s-%s-%s", taskId, hostId, containerId);
  }

  Mono<String> attachLogs(String applicationId, String taskId, String hostId, String containerId);

  Mono<Void> detachLogs(String id);

  Mono<Void> setStatus(String taskId, String hostId, String containerId, ContainerStatus status);

}
