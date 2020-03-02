package com.kraken.runtime.entity.task;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskTest {

  public static final Task TASK = Task.builder()
      .id("id")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .type("RUN")
      .containers(ImmutableList.of())
      .expectedCount(2)
      .description("description")
      .applicationId("app")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(TASK);
  }

}
