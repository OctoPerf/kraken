package com.kraken.runtime.container.predicate;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import com.kraken.runtime.entity.*;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {OtherContainersStoppingPredicate.class, RuntimeContainerPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class OtherContainersStoppingPredicateTest {

  @Autowired
  OtherContainersStoppingPredicate taskPredicate;

  @Autowired
  RuntimeContainerProperties containerProperties;

  @Test
  public void shouldTestTaskOtherId() {
    assertThat(taskPredicate.test(TaskTest.TASK)).isFalse();
  }

  @Test
  public void shouldTestTaskGatlingStopping() {
    assertThat(taskPredicate.test(Task.builder()
        .id(containerProperties.getTaskId())
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of(
            Container.builder()
                .id(containerProperties.getContainerId())
                .hostId(containerProperties.getHostId())
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(containerProperties.getHostId())
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .build()
        ))
        .build())).isTrue();
  }

  @Test
  public void shouldTestOtherTask() {
    assertThat(taskPredicate.test(Task.builder()
        .id("otherTaskId")
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .build())).isFalse();
  }

  @Test
  public void shouldTestTaskGatlingRunning() {
    assertThat(taskPredicate.test(Task.builder()
        .id(containerProperties.getTaskId())
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of(
            Container.builder()
                .id(containerProperties.getContainerId())
                .hostId(containerProperties.getHostId())
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(containerProperties.getHostId())
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId("otherHostId")
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .build()
        ))
        .build())).isFalse();
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(OtherContainersStoppingPredicate.class);
  }
}
