package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringFlatContainersToTask implements FlatContainersToTask {

  @NonNull Function<FlatContainer, Container> dockerContainerToContainer;

  @Override
  public Mono<Task> apply(final GroupedFlux<String, FlatContainer> containersFlux) {
    return containersFlux.collectList().map(containers -> {
      final var first = containers.get(0);
      final var taskType = first.getTaskType();
      final var description = first.getDescription();
      while (containers.size() < first.getExpectedCount()) {
        containers.add(FlatContainer.builder()
            .id("")
            .containerId("")
            .taskId(containersFlux.key())
            .hostId(first.getHostId())
            .taskType(taskType)
            .name("creating")
            .description(description)
            .startDate(0L)
            .status(ContainerStatus.CREATING)
            .expectedCount(first.getExpectedCount())
            .build());
      }
      final var minStatusOrdinal = containers.stream().map(FlatContainer::getStatus).map(Enum::ordinal).min(Integer::compareTo).orElse(0);
      final var containerStatus = ContainerStatus.values()[minStatusOrdinal];
      return Task.builder()
          .id(containersFlux.key())
          .startDate(containers.stream().map(FlatContainer::getStartDate).min(Long::compareTo).orElse(0L))
          .status(containerStatus)
          .type(taskType)
          .containers(containers.stream().map(dockerContainerToContainer).collect(Collectors.toList()))
          .description(description)
          .build();
    });
  }
}
