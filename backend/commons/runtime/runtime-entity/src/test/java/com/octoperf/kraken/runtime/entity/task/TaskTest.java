package com.octoperf.kraken.runtime.entity.task;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.PublicOwnerTest;
import com.octoperf.kraken.security.entity.owner.UserOwnerTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

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
