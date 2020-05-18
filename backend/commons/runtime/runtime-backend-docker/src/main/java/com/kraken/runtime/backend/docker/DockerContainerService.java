package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.backend.api.ContainerService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.logs.LogsService;
import com.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.kraken.runtime.backend.api.EnvironmentLabels.COM_KRAKEN_CONTAINER_NAME;
import static com.kraken.runtime.backend.api.EnvironmentLabels.COM_KRAKEN_TASKID;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerService implements ContainerService {

  @NonNull CommandService commandService;
  @NonNull LogsService logsService;
  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;
  @NonNull ContainerFindService findService;

  @Override
  public Mono<String> attachLogs(final Owner owner, final String taskId, final String containerId, final String containerName) {
    return this.findService.find(owner, taskId, containerName).flatMap(flatContainer -> Mono.fromCallable(() -> {
      final var command = Command.builder()
          .path(".")
          .command(Arrays.asList("docker",
              "logs",
              "-f", containerId))
          .environment(ImmutableMap.of())
          .build();
      final var logs = commandService.execute(command);
      final var id = this.logsId(taskId, containerId, containerName);
      logsService.push(owner, id, LogType.CONTAINER, logs);
      return id;
    }));
  }

  @Override
  public Mono<Void> detachLogs(final Owner owner, final String id) {
    return Mono.fromCallable(() -> {
      logsService.dispose(owner, id, LogType.CONTAINER);
      return null;
    });
  }

  @Override
  public Mono<Void> setStatus(final Owner owner, final String taskId, final String containerId, final String containerName, final ContainerStatus status) {
    return this.findService.find(owner, taskId, containerName).flatMap(flatContainer -> {
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
      });
    }).then();
  }

  @Override
  public Mono<FlatContainer> find(final Owner owner, final String taskId, final String containerName) {
    return findService.find(owner, taskId, containerName);
  }
}
