package com.kraken.runtime.api;

import com.kraken.runtime.entity.*;
import reactor.core.publisher.Mono;

public interface ContainerService {

  default String logsId(final String taskId, final String hostname, final String containerId) {
    return String.format("%s-%s-%s", taskId, hostname, containerId);
  }

  Mono<String> attachLogs(String applicationId, String taskId, String hostname, String containerId);

  Mono<Void> detachLogs(String id);

  Mono<Void> setStatus(String taskId, String hostname, String containerId, ContainerStatus status);

}
