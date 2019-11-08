package com.kraken.runtime.docker;

import com.kraken.runtime.docker.entity.DockerContainer;
import com.kraken.runtime.entity.Container;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerToContainer implements Function<DockerContainer, Container> {

  @Override
  public Container apply(final DockerContainer container) {
    return Container.builder()
        .id(container.getContainerId())
        .status(container.getStatus())
        .startDate(container.getStartDate())
        .name(container.getName())
        .hostId(container.getHostId())
        .build();
  }
}
