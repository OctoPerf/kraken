package com.kraken.runtime.entity.task;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskTest {

  public static final Task TASK = Task.builder()
      .id("id")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .type(TaskType.RUN)
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
