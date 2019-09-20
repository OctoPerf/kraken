package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
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

import java.util.*;
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
  @NonNull List<EnvironmentChecker> envCheckers;
  @NonNull List<EnvironmentPublisher> envPublishers;

  @Override
  public Mono<String> execute(final String applicationId,
                              final TaskType taskType,
                              final Map<String, String> environment) {
    final var taskId = UUID.randomUUID().toString();

    // Update the env variable with all useful key/values
    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(environment);
    envBuilder.put("KRAKEN_TASK_ID", taskId);
    envPublishers.stream()
        .filter(environmentPublisher -> environmentPublisher.test(taskType))
        .forEach(environmentPublisher -> envBuilder.putAll(environmentPublisher.get()));

    // Check that the env is OK
    final var env = envBuilder.build();
    envCheckers.stream()
        .filter(environmentChecker -> environmentChecker.test(taskType))
        .forEach(environmentChecker -> environmentChecker.accept(env));

    final var command = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "up",
            "-d",
            "--no-color"))
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
  public Mono<String> cancel(final String applicationId,
                             final Task task) {

    final var taskId = task.getId();

    final var command = Command.builder()
        .path(taskTypeToPath.apply(task.getType()))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "down"))
        .environment(ImmutableMap.of())
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = logsService.concat(commandService.execute(command));
      logsService.push(applicationId, taskId, LogType.TASK, logs);
      return taskId;
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
