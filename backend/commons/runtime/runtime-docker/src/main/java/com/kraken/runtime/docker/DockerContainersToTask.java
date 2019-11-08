package com.kraken.runtime.docker;

import com.kraken.runtime.docker.entity.DockerContainer;
import com.kraken.runtime.docker.properties.DockerProperties;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainersToTask implements Function<GroupedFlux<String, DockerContainer>, Mono<Task>> {

  @NonNull DockerProperties dockerProperties;
  @NonNull Function<DockerContainer, Container> dockerContainerToContainer;

  @Override
  public Mono<Task> apply(final GroupedFlux<String, DockerContainer> containersFlux) {
    return containersFlux.collectList().map(containers -> {
      final var first = containers.get(0);
      final var taskType = first.getTaskType();
      final var description = first.getDescription();
      while (containers.size() < dockerProperties.getContainersCount().get(taskType)) {
        containers.add(DockerContainer.builder()
            .id("")
            .containerId("")
            .taskId(containersFlux.key())
            .hostId("hostId")
            .taskType(taskType)
            .name("creating")
            .description(description)
            .startDate(0L)
            .status(ContainerStatus.CREATING)
            .build());
      }
      final var minStatusOrdinal = containers.stream().map(DockerContainer::getStatus).map(Enum::ordinal).min(Integer::compareTo).orElse(0);
      final var containerStatus = ContainerStatus.values()[minStatusOrdinal];
      return Task.builder()
          .id(containersFlux.key())
          .startDate(containers.stream().map(DockerContainer::getStartDate).min(Long::compareTo).orElse(0L))
          .status(containerStatus)
          .type(taskType)
          .containers(containers.stream().map(dockerContainerToContainer).collect(Collectors.toList()))
          .description(description)
          .build();
    });
  }
}
