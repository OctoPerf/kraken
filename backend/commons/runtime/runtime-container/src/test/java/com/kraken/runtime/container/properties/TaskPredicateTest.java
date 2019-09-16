package com.kraken.runtime.container.properties;

import com.google.common.collect.ImmutableList;
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
    classes = {TaskPredicate.class, RuntimeContainerTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class TaskPredicateTest {

  @Autowired
  TaskPredicate taskPredicate;

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
                .id("id")
                .containerId(containerProperties.getContainerId())
                .groupId(containerProperties.getGroupId())
                .name("name")
                .description("description")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .taskId(containerProperties.getTaskId())
                .taskType(TaskType.RUN)
                .build(),
            Container.builder()
                .id("id")
                .containerId("otherId")
                .groupId(containerProperties.getGroupId())
                .name("name")
                .description("description")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .taskId(containerProperties.getTaskId())
                .taskType(TaskType.RUN)
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
                .id("id")
                .containerId(containerProperties.getContainerId())
                .groupId(containerProperties.getGroupId())
                .name("name")
                .description("description")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .taskId(containerProperties.getTaskId())
                .taskType(TaskType.RUN)
                .build(),
            Container.builder()
                .id("id")
                .containerId("otherId")
                .groupId(containerProperties.getGroupId())
                .name("name")
                .description("description")
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .taskId(containerProperties.getTaskId())
                .taskType(TaskType.RUN)
                .build(),
            Container.builder()
                .id("id")
                .containerId("otherId")
                .groupId("otherGroupId")
                .name("name")
                .description("description")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .taskId(containerProperties.getTaskId())
                .taskType(TaskType.RUN)
                .build()
        ))
        .build())).isFalse();
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(TaskPredicate.class);
  }
}
