package com.kraken.runtime.event;

import com.kraken.runtime.entity.ExecutionContextTest;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class TaskExecutedEventTest {
  public static final TaskExecutedEvent TASK_EXECUTED_EVENT = TaskExecutedEvent.builder()
      .context(ExecutionContextTest.EXECUTION_CONTEXT)
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(TASK_EXECUTED_EVENT);
  }

}
