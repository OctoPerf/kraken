package com.kraken.runtime.kubernetes;

import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class KubernetesContainerService implements ContainerService {

  @Override
  public Mono<Void> attachLogs(String applicationId, Container container) {
    return null;
  }

  @Override
  public Mono<Void> detachLogs(Container container) {
    return null;
  }

  @Override
  public Mono<Container> setStatus(String containerId, ContainerStatus status) {
    return null;
  }
}
