package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerContainersToTaskTest {

  private final FlatContainersToTask dockerContainersToTask = new SpringFlatContainersToTask(new SpringFlatContainerToContainer());

  @Test
  public void shouldAddCreatingContainer() {
    final var container = FlatContainer.builder()
        .id("id")
        .containerId("containerId")
        .hostId("hostId")
        .taskId("taskId")
        .taskType(TaskType.RUN)
        .name("name")
        .description("description")
        .startDate(42L)
        .status(ContainerStatus.READY)
        .expectedCount(2)
        .build();
    final var flux = Flux.just(container).groupBy(FlatContainer::getTaskId).next().block();
    assertThat(flux).isNotNull();
    final var task = dockerContainersToTask.apply(flux).block();
    assertThat(task).isNotNull();
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.CREATING);
    assertThat(task.getDescription()).isEqualTo("description");
    assertThat(task.getContainers().size()).isEqualTo(2);
  }

}

