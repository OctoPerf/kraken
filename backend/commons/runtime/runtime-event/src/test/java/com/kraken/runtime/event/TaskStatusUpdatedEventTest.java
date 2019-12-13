package com.kraken.runtime.event;

import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskStatusUpdatedEventTest {
  public static final TaskStatusUpdatedEvent TASK_STATUS_UPDATED_EVENT = TaskStatusUpdatedEvent.builder()
      .task(TaskTest.TASK)
      .previousStatus(ContainerStatus.CREATING)
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(TASK_STATUS_UPDATED_EVENT.getClass());
    TestUtils.shouldPassToString(TASK_STATUS_UPDATED_EVENT);
    new NullPointerTester()
        .setDefault(Task.class, TaskTest.TASK)
        .testConstructors(TASK_STATUS_UPDATED_EVENT.getClass(), PACKAGE);
  }

}
