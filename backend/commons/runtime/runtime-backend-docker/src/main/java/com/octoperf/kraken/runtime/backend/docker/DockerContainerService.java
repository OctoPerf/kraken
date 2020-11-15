package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.backend.api.ContainerService;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.entity.log.LogType;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.BiFunction;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerService implements ContainerService {

  @NonNull CommandService commandService;
  @NonNull TaskLogsService logsService;
  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;
  @NonNull ContainerFindService findService;

  @Override
  public Mono<String> attachLogs(final Owner owner, final String taskId, final String containerId, final String containerName) {
    return this.findService.find(owner, taskId, containerName).flatMap(flatContainer -> Mono.fromCallable(() -> {
      final var command = Command.builder()
          .path(".")
          .args(Arrays.asList("docker",
              "logs",
              "-f", containerId))
          .environment(ImmutableMap.of())
          .build();
      final var logs = commandService.validate(command).flatMapMany(commandService::execute);
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
          .args(Arrays.asList("docker",
              "rename",
              containerId,
              containerStatusToName.apply(containerName, status)))
          .environment(ImmutableMap.of())
          .build();

      return commandService.validate(command)
          .flatMapMany(commandService::execute)
          .collectList()
          .map(strings -> {
            final var logs = String.join("\n", strings);
            log.info(logs);
            return logs;
          });
    })
        .then();
  }

  @Override
  public Mono<FlatContainer> find(final Owner owner, final String taskId, final String containerName) {
    return findService.find(owner, taskId, containerName);
  }
}
