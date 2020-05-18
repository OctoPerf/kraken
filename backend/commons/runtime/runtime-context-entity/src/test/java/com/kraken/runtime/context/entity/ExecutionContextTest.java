package com.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwnerTest;
import com.kraken.security.entity.owner.UserOwnerTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class ExecutionContextTest {

  public static final ExecutionContext EXECUTION_CONTEXT = ExecutionContext.builder()
      .owner(UserOwnerTest.USER_OWNER)
      .taskId("taskId")
      .taskType(TaskType.GATLING_RUN)
      .description("description")
      .templates(ImmutableMap.of("hostId", "template"))
      .build();



  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(EXECUTION_CONTEXT.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(Owner.class, PublicOwnerTest.PUBLIC_OWNER)
        .testConstructors(EXECUTION_CONTEXT.getClass(), PACKAGE);
  }
  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(EXECUTION_CONTEXT);
  }

}
