package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.FlatContainerTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringFlatContainerToContainerTest {

  private final SpringFlatContainerToContainer flatContainerToContainer = new SpringFlatContainerToContainer();

  @Test
  public void shouldConvert(){
    final var dockerContainer = FlatContainerTest.CONTAINER;
    final var container = flatContainerToContainer.apply(dockerContainer);
    assertThat(container).isEqualTo(Container.builder()
        .id(dockerContainer.getContainerId())
        .hostId(dockerContainer.getHostId())
        .name(dockerContainer.getName())
        .startDate(dockerContainer.getStartDate())
        .status(dockerContainer.getStatus())
        .build());
  }
}

