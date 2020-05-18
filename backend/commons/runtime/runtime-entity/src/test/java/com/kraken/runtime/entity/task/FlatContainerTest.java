package com.kraken.runtime.entity.task;

import com.google.common.testing.NullPointerTester;
import com.kraken.security.entity.owner.Owner;
import com.kraken.security.entity.owner.PublicOwnerTest;
import com.kraken.security.entity.owner.UserOwnerTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tests.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class FlatContainerTest {

  public static final FlatContainer CONTAINER = FlatContainer.builder()
      .id("id")
      .name("name")
      .hostId("hostId")
      .taskId("taskId")
      .startDate(42L)
      .status(ContainerStatus.STARTING)
      .label("label")
      .taskType(TaskType.GATLING_RUN)
      .description("description")
      .expectedCount(2)
      .owner(UserOwnerTest.USER_OWNER)
      .build();



  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(CONTAINER.getClass());
  }

  @Test
  public void shouldPassNPE() {
    new NullPointerTester()
        .setDefault(Owner.class, PublicOwnerTest.PUBLIC_OWNER)
        .testConstructors(CONTAINER.getClass(), PACKAGE);
  }
  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(CONTAINER);
  }

  @Test
  public void shouldWither() {
    assertThat(CONTAINER.withStatus(ContainerStatus.READY).getStatus()).isEqualTo(ContainerStatus.READY);
  }
}
