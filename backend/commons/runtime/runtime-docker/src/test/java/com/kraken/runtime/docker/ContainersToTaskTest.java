package com.kraken.runtime.docker;

import com.kraken.runtime.docker.properties.DockerPropertiesTest;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainersToTaskTest {

  private final ContainersToTask containersToTask = new ContainersToTask(DockerPropertiesTest.DOCKER_PROPERTIES);

  @Test
  public void shouldAddCreatingContainer() {
    final var container = Container.builder()
        .id("id")
        .containerId("containerId")
        .taskId("taskId")
        .taskType(TaskType.RUN)
        .name("name")
        .startDate(42L)
        .status(ContainerStatus.READY)
        .build();
    final var flux = Flux.just(container).groupBy(Container::getTaskId).next().block();
    assertThat(flux).isNotNull();
    final var task = containersToTask.apply(flux).block();
    assertThat(task).isNotNull();
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.CREATING);
    assertThat(task.getContainers().size()).isEqualTo(2);
  }

}

