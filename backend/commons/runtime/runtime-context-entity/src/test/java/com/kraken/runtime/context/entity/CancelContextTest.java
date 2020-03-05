package com.kraken.runtime.context.entity;

import com.kraken.runtime.entity.task.TaskType;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class CancelContextTest {

  public static final CancelContext CANCEL_CONTEXT = CancelContext.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType(TaskType.RUN)
      .template("template")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(CANCEL_CONTEXT);
  }

}
