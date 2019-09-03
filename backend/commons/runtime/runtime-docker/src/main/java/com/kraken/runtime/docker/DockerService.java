package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.container.ContainerService;
import com.kraken.runtime.docker.properties.DockerProperties;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.logs.LogsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerService implements ContainerService {

  @NonNull CommandService commandService;

  @NonNull DockerProperties dockerProperties;

  @NonNull LogsService logsService;

  @NonNull Function<String, Container> stringToContainer;

  @NonNull Function<GroupedFlux<String, Container>, Mono<Task>> containersToTask;

  @NonNull BiFunction<String, ContainerStatus, String> containerStatusToName;

  @Override
  public Mono<Void> execute(TaskType taskType, Map<String, String> environment) {
//  Comment faire pour les logs de lancement ? faudrait les afficher en stream car c'est surement long s'il faut pull les images ...
//    On peut faire un attach Logs avec un l'id de la task crée ?
// TODO Run docker-compose up
//    TODO Valider le resultat => si fail retourner une erreur, si pas fail retourner les logs ?
    return null;
  }

  @Override
  public Mono<Void> cancel(final Task task) {
//    TODO docker-compose down et force si ca veut pas
    return null;
  }

  @Override
  public Flux<Task> listTasks() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.taskId",
            "--format", StringToContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToContainer)
        .groupBy(Container::getTaskId)
        .flatMap(containersToTask);
  }

  @Override
  public Flux<List<Task>> watchTasks() {
    return Flux.interval(dockerProperties.getWatchTasksDelay())
        .flatMap(aLong -> this.listTasks().collectList())
        .distinctUntilChanged();
  }

  @Override
  public Flux<Log> logs(final String applicationId) {
    return logsService.listen(applicationId);
  }

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
      logsService.push(applicationId, container.getContainerId(), logs);
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
