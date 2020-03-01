package com.kraken.runtime.container.predicate;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import com.kraken.runtime.entity.task.*;
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
        .expectedCount(2)
        .containers(ImmutableList.of(
            Container.builder()
                .id("id")
                .hostId(containerProperties.getHostId())
                .label("label")
                .name(containerProperties.getContainerName())
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(containerProperties.getHostId())
                .label("label")
                .name("otherName")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .build()
        ))
        .applicationId("app")
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
        .expectedCount(2)
        .applicationId("app")
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
        .expectedCount(2)
        .containers(ImmutableList.of(
            Container.builder()
                .id("id")
                .hostId(containerProperties.getHostId())
                .label("label")
                .name(containerProperties.getContainerName())
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(containerProperties.getHostId())
                .label("label")
                .name("otherName")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId("otherHostId")
                .label("label")
                .name("name")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .build()
        ))
        .applicationId("app")
        .build())).isFalse();
  }

  @Test
  public void shouldTestTaskRunStopping() {

    final var containerProperties = RuntimeContainerProperties.builder()
        .taskId("vzkziobanr")
        .containerName("shell-run-vzkziobanr-n0usmshdr0-gatling-telegraf")
        .hostId("n0usmshdr0")
        .taskType(TaskType.RUN)
        .build();

    final var taskPredicate = new OtherContainersStoppingPredicate(containerProperties);

    assertThat(taskPredicate.test(Task.builder()
        .id("vzkziobanr")
        .description("description")
        .startDate(1574464436000L)
        .status(ContainerStatus.RUNNING)
        .type(TaskType.RUN)
        .expectedCount(4)
        .containers(ImmutableList.of(
            Container.builder()
                .id("shell-run-vzkziobanr-0joqmltfe5")
                .hostId("n0usmshdr0")
                .label("Gatling Runner")
                .name("shell-run-vzkziobanr-n0usmshdr0-gatling-runner")
                .startDate(1574464436000L)
                .status(ContainerStatus.STOPPING)
                .build(),
            Container.builder()
                .id("shell-run-vzkziobanr-0joqmltfe5")
                .hostId("n0usmshdr0")
                .label("Telegraf")
                .name("shell-run-vzkziobanr-n0usmshdr0-gatling-telegraf")
                .startDate(1574464436000L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("shell-run-vzkziobanr-dy2qpu3czo")
                .hostId("yrsqpw9d4h")
                .label("Gatling Runner")
                .name("shell-run-vzkziobanr-yrsqpw9d4h-gatling-runner")
                .startDate(1574464436000L)
                .status(ContainerStatus.STOPPING)
                .build(),
            Container.builder()
                .id("shell-run-vzkziobanr-dy2qpu3czo")
                .hostId("yrsqpw9d4h")
                .label("Telegraf")
                .name("shell-run-vzkziobanr-yrsqpw9d4h-gatling-telegraf")
                .startDate(1574464436000L)
                .status(ContainerStatus.RUNNING)
                .build()
        ))
        .applicationId("app")
        .build())).isTrue();
  }


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(OtherContainersStoppingPredicate.class);
  }
}
