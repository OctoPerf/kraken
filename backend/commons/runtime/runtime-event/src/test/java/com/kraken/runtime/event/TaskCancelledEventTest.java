package com.kraken.runtime.event;

import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskCancelledEventTest {
  public static final TaskCancelledEvent TASK_CANCELLED_EVENT = TaskCancelledEvent.builder()
      .applicationId("app")
      .taskId("taskId")
      .type(TaskType.RUN)
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(TASK_CANCELLED_EVENT);
  }

}
