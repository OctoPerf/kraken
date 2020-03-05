package com.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.entity.ExecutionContext;
import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class ExecutionContextTest {

  public static final ExecutionContext EXECUTION_CONTEXT = ExecutionContext.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType(TaskType.RUN)
      .description("description")
      .templates(ImmutableMap.of("hostId", "template"))
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_CONTEXT);
  }

}
