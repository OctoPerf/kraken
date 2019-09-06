package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.properties.DockerProperties;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
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
import java.util.UUID;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerTaskService implements TaskService {

  @NonNull CommandService commandService;

  @NonNull DockerProperties dockerProperties;

  @NonNull LogsService logsService;

  @NonNull Function<String, Container> stringToContainer;

  @NonNull Function<GroupedFlux<String, Container>, Mono<Task>> containersToTask;

  @NonNull Function<TaskType, String> taskTypeToPath;

  @Override
  public Mono<String> execute(final String applicationId,
                              final TaskType taskType,
                              final String description,
                              final Map<String, String> environment) {
    final var taskId = UUID.randomUUID().toString();

    final var env = ImmutableMap.<String, String>builder()
        .putAll(environment)
        .put("KRAKEN_TASK_ID", taskId)
        .put("KRAKEN_DESCRIPTION", description)
        .build();

    final var command = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "up",
            "-d"))
        .environment(env)
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = logsService.concat(commandService.execute(command));
      logsService.push(applicationId, taskId, LogType.TASK, logs);
      return taskId;
    });
  }

  @Override
  public Mono<Void> cancel(final String applicationId,
                           final Task task) {

    final var taskId = task.getId();

    final var command = Command.builder()
        .path(taskTypeToPath.apply(task.getType()))
        .command(Arrays.asList("docker-compose",
            "down"))
        .environment(ImmutableMap.of())
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = logsService.concat(commandService.execute(command));
      logsService.push(applicationId, taskId, LogType.TASK, logs);
      return null;
    });
  }

  @Override
  public Flux<Task> list() {
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
  public Flux<List<Task>> watch() {
    return Flux.interval(dockerProperties.getWatchTasksDelay())
        .flatMap(aLong -> this.list().collectList())
        .distinctUntilChanged();
  }

}
