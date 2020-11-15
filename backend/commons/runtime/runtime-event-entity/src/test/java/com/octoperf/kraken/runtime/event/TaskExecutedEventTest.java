package com.octoperf.kraken.runtime.event;

import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class TaskExecutedEventTest {
  public static final TaskExecutedEvent TASK_EXECUTED_EVENT = TaskExecutedEvent.builder()
      .context(ExecutionContextTest.EXECUTION_CONTEXT)
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(TASK_EXECUTED_EVENT.getClass());
    TestUtils.shouldPassToString(TASK_EXECUTED_EVENT);
    new NullPointerTester()
        .setDefault(ExecutionContext.class, ExecutionContextTest.EXECUTION_CONTEXT)
        .testConstructors(TASK_EXECUTED_EVENT.getClass(), PACKAGE);
  }

}
