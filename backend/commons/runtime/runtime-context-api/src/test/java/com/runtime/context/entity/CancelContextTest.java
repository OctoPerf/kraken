package com.runtime.context.entity;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class CancelContextTest {

  public static final CancelContext CANCEL_CONTEXT = CancelContext.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType("RUN")
      .environment(ImmutableMap.of("key", "value"))
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(CANCEL_CONTEXT);
  }

}
