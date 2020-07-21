package com.octoperf.kraken.runtime.backend.spring;

import com.octoperf.kraken.runtime.entity.task.Container;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringFlatContainerToContainerTest {

  private final SpringFlatContainerToContainer flatContainerToContainer = new SpringFlatContainerToContainer();

  @Test
  public void shouldConvert(){
    final var dockerContainer = FlatContainerTest.CONTAINER;
    final var container = flatContainerToContainer.apply(dockerContainer);
    assertThat(container).isEqualTo(Container.builder()
        .id(dockerContainer.getId())
        .hostId(dockerContainer.getHostId())
        .label(dockerContainer.getLabel())
        .name(dockerContainer.getName())
        .startDate(dockerContainer.getStartDate())
        .status(dockerContainer.getStatus())
        .build());
  }
}

