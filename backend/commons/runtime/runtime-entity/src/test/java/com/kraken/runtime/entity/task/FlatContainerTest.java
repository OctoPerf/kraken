package com.kraken.runtime.entity.task;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class FlatContainerTest {

  public static final FlatContainer CONTAINER = FlatContainer.builder()
      .id("id")
      .name("name")
      .hostId("hostId")
      .taskId("taskId")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .label("label")
      .taskType(TaskType.RUN)
      .description("description")
      .expectedCount(2)
      .applicationId("app")
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
