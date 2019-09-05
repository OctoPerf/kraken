package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.logs.LogsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerService implements ContainerService {

  @NonNull CommandService commandService;

  @NonNull LogsService logsService;

  @NonNull Function<String, Container> stringToContainer;

  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;

  @Override
  public Mono<Void> attachLogs(final String applicationId, final Container container) {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "logs",
            "-f", container.getId()))
        .environment(ImmutableMap.of())
        .build();
    final var logs = logsService.concat(commandService.execute(command));
    return Mono.fromCallable(() -> {
      logsService.push(applicationId, container.getContainerId(), LogType.CONTAINER, logs);
      return null;
    });
  }

  @Override
  public Mono<Void> detachLogs(final Container container) {
    return Mono.fromCallable(() -> {
      logsService.cancel(container.getContainerId());
      return null;
    });
  }

  @Override
  public Mono<Container> setStatus(final String containerId, final ContainerStatus status) {
    return this.find(containerId).flatMap(container -> {
      final var command = Command.builder()
          .path(".")
          .command(Arrays.asList("docker",
              "rename",
              container.getId(),
              containerStatusToName.apply(container.getName(), status)))
          .environment(ImmutableMap.of())
          .build();
      return commandService.execute(command).collectList().map(list -> container.withStatus(status));
    });
  }

  public Mono<Container> find(final String containerId) {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + containerId,
            "--format", StringToContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToContainer)
        .next();
  }
}
