package com.kraken.runtime.event;

import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.runtime.context.entity.CancelContextTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class TaskCancelledEventTest {
  public static final TaskCancelledEvent TASK_CANCELLED_EVENT = TaskCancelledEvent.builder()
      .context(CancelContextTest.CANCEL_CONTEXT)
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(TASK_CANCELLED_EVENT.getClass());
    TestUtils.shouldPassToString(TASK_CANCELLED_EVENT);
    new NullPointerTester()
        .setDefault(CancelContext.class, CancelContextTest.CANCEL_CONTEXT)
        .testConstructors(TASK_CANCELLED_EVENT.getClass(), PACKAGE);
  }

}
