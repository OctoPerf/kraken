package com.kraken.runtime.docker;

import com.kraken.runtime.docker.entity.DockerContainer;
import com.kraken.runtime.docker.properties.DockerPropertiesTest;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import org.junit.Test;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

public class DockerContainersToTaskTest {

  private final DockerContainersToTask dockerContainersToTask = new DockerContainersToTask(DockerPropertiesTest.DOCKER_PROPERTIES,
      new DockerContainerToContainer());

  @Test
  public void shouldAddCreatingContainer() {
    final var container = DockerContainer.builder()
        .id("id")
        .containerId("containerId")
        .hostId("hostId")
        .taskId("taskId")
        .taskType(TaskType.RUN)
        .name("name")
        .description("description")
        .startDate(42L)
        .status(ContainerStatus.READY)
        .build();
    final var flux = Flux.just(container).groupBy(DockerContainer::getTaskId).next().block();
    assertThat(flux).isNotNull();
    final var task = dockerContainersToTask.apply(flux).block();
    assertThat(task).isNotNull();
    assertThat(task.getStatus()).isEqualTo(ContainerStatus.CREATING);
    assertThat(task.getDescription()).isEqualTo("description");
    assertThat(task.getContainers().size()).isEqualTo(2);
  }

}

