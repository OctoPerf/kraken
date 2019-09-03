package com.kraken.runtime.docker;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
class ContainersToTask implements Function<GroupedFlux<String, Container>, Mono<Task>> {

  @Override
  public Mono<Task> apply(final GroupedFlux<String, Container> containersFlux) {
    return containersFlux.collectList().map(containers -> {
      final var minStatusOrdinal = containers.stream().map(Container::getStatus).map(Enum::ordinal).min(Integer::compareTo).orElse(0);
      final var containerStatus = ContainerStatus.values()[minStatusOrdinal];
      return Task.builder()
          .id(containersFlux.key())
          .startDate(containers.stream().map(Container::getStartDate).min(Long::compareTo).orElse(0L))
          .status(containerStatus)
          .type(containers.get(0).getTaskType())
          .containers(containers)
          .build();
    });
  }
}
