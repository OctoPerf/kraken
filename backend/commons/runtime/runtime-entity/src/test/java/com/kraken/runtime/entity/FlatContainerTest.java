package com.kraken.runtime.entity;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class FlatContainerTest {

  public static final FlatContainer CONTAINER = FlatContainer.builder()
      .id("id")
      .containerId("containerId")
      .hostId("hostId")
      .taskId("taskId")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .name("name")
      .taskType(TaskType.RUN)
      .description("description")
      .expectedCount(2)
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
