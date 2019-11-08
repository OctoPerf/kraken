package com.kraken.runtime.docker.entity;

import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class DockerContainerTest {

  public static final DockerContainer CONTAINER = DockerContainer.builder()
      .id("id")
      .containerId("containerId")
      .hostId("hostId")
      .taskId("taskId")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .name("name")
      .taskType(TaskType.RUN)
      .description("description")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(CONTAINER);
  }

  @Test
  public void shouldWither() {
    assertThat(CONTAINER.withStatus(ContainerStatus.READY).getStatus()).isEqualTo(ContainerStatus.READY);
  }
}
