package com.kraken.runtime.mock;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ContainerTest;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {MockTaskService.class})
public class MockTaskServiceTest {

  @Autowired
  TaskService taskService;

  @Autowired
  ContainerService containerService;

  @Test
  public void shouldInit() {
    assertThat(taskService).isSameAs(containerService);
  }

  @Test
  public void shouldExecute() {
    taskService.execute("applicationId", TaskType.RUN, ImmutableMap.of());
    final var task = taskService.list().blockFirst();
    assertThat(task).isNotNull();
    assertThat(task.getType()).isEqualTo(TaskType.RUN);
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.CREATING);
  }

  @Test
  public void shouldCancel() {
    taskService.cancel("applicationId", TaskTest.TASK);
    final var task = taskService.list().blockFirst();
    assertThat(task).isNull();
  }

  @Test
  public void shouldWatch() {
    final var list = taskService.watch().take(1).blockFirst();
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(1);
  }

  @Test
  public void shouldAttachLogs() {
    containerService.attachLogs("applicationId", ContainerTest.CONTAINER).block();
  }

  @Test
  public void shouldDetachLogs() {
    containerService.detachLogs(ContainerTest.CONTAINER).block();
  }

  @Test
  public void shouldSetStatus() {
    containerService.setStatus("containerId", ContainerStatus.READY).block();
    final var task = taskService.list().blockFirst();
    assertThat(task).isNotNull();
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.READY);
    assertThat(task.getContainers().size()).isEqualTo(1);
  }

}
