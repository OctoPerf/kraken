package com.kraken.runtime.entity;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionContextTest {

  public static final ExecutionContext EXECUTION_CONTEXT = ExecutionContext.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType(TaskType.RUN)
      .description("description")
      .environment(ImmutableMap.of("foo", "bar"))
      .hosts(ImmutableMap.of("hostId", ImmutableMap.of("key", "value")))
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_CONTEXT);
  }

  @Test
  public void shouldWither() {
    assertThat(EXECUTION_CONTEXT.withApplicationId("other").getApplicationId()).isEqualTo("other");
    assertThat(EXECUTION_CONTEXT.withTaskId("other").getTaskId()).isEqualTo("other");
  }
}
