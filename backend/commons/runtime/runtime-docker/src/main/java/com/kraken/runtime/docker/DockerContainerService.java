package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.logs.LogsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerService implements ContainerService {

  @NonNull CommandService commandService;
  @NonNull LogsService logsService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;

  @Override
  public Mono<Void> attachLogs(final String applicationId, final String taskId, final String hostId, final String containerId) {
    return this.find(containerId).map(container -> {
      final var command = Command.builder()
          .path(".")
          .command(Arrays.asList("docker",
              "logs",
              "-f", container.getId()))
          .environment(ImmutableMap.of())
          .build();
      final var logs = logsService.concat(commandService.execute(command));
      logsService.push(applicationId, this.logsId(taskId, hostId, containerId), LogType.CONTAINER, logs);
      return container;
    }).then();
  }

  @Override
  public Mono<Void> detachLogs(final String taskId, final String hostId, final String containerId) {
    return Mono.fromCallable(() -> {
      logsService.cancel(this.logsId(taskId, hostId, containerId));
      return null;
    });
  }

  @Override
  public Mono<Void> setStatus(final String taskId, final String hostId, final String containerId, final ContainerStatus status) {
    return this.find(containerId).flatMap(container -> {
      final var command = Command.builder()
          .path(".")
          .command(Arrays.asList("docker",
              "rename",
              container.getId(),
              containerStatusToName.apply(container.getContainerId(), status)))
          .environment(ImmutableMap.of())
          .build();
      return logsService.concat(commandService.execute(command)).collectList().map(list -> {
        final var logs = String.join("\n", list);
        log.info(logs);
        return logs;
      }).then();
    });
  }

  private Mono<FlatContainer> find(final String containerId) {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + containerId,
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToFlatContainer)
        .next();
  }
}
