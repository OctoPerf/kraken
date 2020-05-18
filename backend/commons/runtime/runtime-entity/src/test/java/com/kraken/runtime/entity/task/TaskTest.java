package com.kraken.runtime.entity.task;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwnerTest;
import com.kraken.security.entity.owner.UserOwnerTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class TaskTest {

  public static final Task TASK = Task.builder()
      .id("id")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .type(TaskType.GATLING_RUN)
      .containers(ImmutableList.of())
      .expectedCount(2)
      .description("description")
      .owner(UserOwnerTest.USER_OWNER)
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(TASK.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(Owner.class, PublicOwnerTest.PUBLIC_OWNER)
        .testConstructors(TASK.getClass(), PACKAGE);
  }
  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(TASK);
  }
}
