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
  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;

  @Override
  public Mono<String> attachLogs(final String applicationId, final String taskId, final String containerId, final String containerName) {
    return Mono.fromCallable(() -> {
      final var command = Command.builder()
          .path(".")
          .command(Arrays.asList("docker",
              "logs",
              "-f", containerId))
          .environment(ImmutableMap.of())
          .build();
      final var logs = commandService.execute(command);
      final var id = this.logsId(taskId, containerId, containerName);
      logsService.push(applicationId, id, LogType.CONTAINER, logs);
      return id;
    });
  }

  @Override
  public Mono<Void> detachLogs(final String id) {
    return Mono.fromCallable(() -> {
      logsService.cancel(id);
      return null;
    });
  }

  @Override
  public Mono<Void> setStatus(final String taskId, final String containerId, final String containerName, final ContainerStatus status) {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "rename",
            containerId,
            containerStatusToName.apply(containerName, status)))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command).collectList().map(strings -> {
      final var logs = String.join("\n", strings);
      log.info(logs);
      return logs;
    }).then();
  }

  @Override
  public Mono<FlatContainer> find(final String taskId, final String containerName) {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken/taskId=" + taskId,
            "--filter", "label=com.kraken/containerName=" + containerName,
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToFlatContainer)
        .next();
  }
}
