package com.kraken.runtime.docker;

import com.kraken.runtime.docker.entity.DockerContainerTest;
import com.kraken.runtime.entity.Container;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerContainerToContainerTest {

  private final DockerContainerToContainer dockerContainerToContainer = new DockerContainerToContainer();

  @Test
  public void shouldConvert(){
    final var dockerContainer = DockerContainerTest.CONTAINER;
    final var container = dockerContainerToContainer.apply(dockerContainer);
    assertThat(container).isEqualTo(Container.builder()
        .id(dockerContainer.getContainerId())
        .hostId(dockerContainer.getHostId())
        .name(dockerContainer.getName())
        .startDate(dockerContainer.getStartDate())
        .status(dockerContainer.getStatus())
        .build());
  }
}

