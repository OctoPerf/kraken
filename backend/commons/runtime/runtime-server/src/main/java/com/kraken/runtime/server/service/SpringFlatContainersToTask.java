package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.task.Container;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.Task;
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
      final var expectedCount = first.getExpectedCount();
      final var owner = first.getOwner();
      final var minStatusOrdinal = containers.stream().map(FlatContainer::getStatus).map(Enum::ordinal).min(Integer::compareTo).orElse(0);

      final var taskStatus = containers.size() < expectedCount && minStatusOrdinal < ContainerStatus.RUNNING.ordinal() ? ContainerStatus.CREATING : ContainerStatus.values()[minStatusOrdinal];

      return Task.builder()
          .id(containersFlux.key())
          .startDate(containers.stream().map(FlatContainer::getStartDate).min(Long::compareTo).orElse(0L))
          .status(taskStatus)
          .type(taskType)
          .containers(containers.stream().map(dockerContainerToContainer).collect(Collectors.toList()))
          .expectedCount(first.getExpectedCount())
          .description(description)
          .owner(owner)
          .build();
    });
  }
}
