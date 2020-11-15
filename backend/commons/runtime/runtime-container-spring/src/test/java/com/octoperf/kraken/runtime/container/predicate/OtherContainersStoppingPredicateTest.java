package com.octoperf.kraken.runtime.container.predicate;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.entity.task.Container;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.entity.task.TaskTest;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.octoperf.kraken.runtime.entity.task.TaskType.GATLING_RUN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OtherContainersStoppingPredicate.class)
public class OtherContainersStoppingPredicateTest {
  @Autowired
  OtherContainersStoppingPredicate taskPredicate;
  @MockBean
  ContainerProperties container;

  @BeforeEach
  public void setUp() {
    when(container.getTaskId()).thenReturn("vzkziobanr");
    when(container.getName()).thenReturn("shell-run-vzkziobanr-n0usmshdr0-gatling-telegraf");
    when(container.getHostId()).thenReturn("n0usmshdr0");
    when(container.getTaskType()).thenReturn(GATLING_RUN);
  }

  @Test
  public void shouldTestTaskOtherId() {
    assertThat(taskPredicate.test(TaskTest.TASK)).isFalse();
  }

  @Test
  public void shouldTestTaskGatlingStopping() {
    assertThat(taskPredicate.test(Task.builder()
        .id(container.getTaskId())
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(GATLING_RUN)
        .expectedCount(2)
        .containers(ImmutableList.of(
            Container.builder()
                .id("id")
                .hostId(container.getHostId())
                .label("label")
                .name(container.getName())
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(container.getHostId())
                .label("label")
                .name("otherName")
                .startDate(0L)
                .status(ContainerStatus.STOPPING)
                .build()
        ))
        .owner(Owner.PUBLIC)
        .build())).isTrue();
  }

  @Test
  public void shouldTestOtherTask() {
    assertThat(taskPredicate.test(Task.builder()
        .id("otherTaskId")
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(GATLING_RUN)
        .containers(ImmutableList.of())
        .expectedCount(2)
        .owner(Owner.PUBLIC)
        .build())).isFalse();
  }

  @Test
  public void shouldTestTaskGatlingRunning() {
    assertThat(taskPredicate.test(Task.builder()
        .id(container.getTaskId())
        .description("description")
        .startDate(0L)
        .status(ContainerStatus.STARTING)
        .type(GATLING_RUN)
        .expectedCount(2)
        .containers(ImmutableList.of(
            Container.builder()
                .id("id")
                .hostId(container.getHostId())
                .label("label")
                .name(container.getName())
                .startDate(0L)
                .status(ContainerStatus.RUNNING)
                .build(),
            Container.builder()
                .id("otherId")
                .hostId(container.getHostId())
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
        .owner(Owner.PUBLIC)
        .build())).isFalse();
  }

  @Test
  public void shouldTestTaskRunStopping() {
    final var taskPredicate = new OtherContainersStoppingPredicate(container);

    assertThat(taskPredicate.test(Task.builder()
        .id("vzkziobanr")
        .description("description")
        .startDate(1574464436000L)
        .status(ContainerStatus.RUNNING)
        .type(GATLING_RUN)
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
        .owner(Owner.PUBLIC)
        .build())).isTrue();
  }


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(OtherContainersStoppingPredicate.class);
  }
}
