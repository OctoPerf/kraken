package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
import com.kraken.runtime.docker.properties.DockerProperties;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.unique.id.IdGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerTaskService implements TaskService {

  @NonNull CommandService commandService;
  @NonNull DockerProperties dockerProperties;
  @NonNull LogsService logsService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull Function<TaskType, String> taskTypeToPath;
  @NonNull List<EnvironmentChecker> envCheckers;
  @NonNull List<EnvironmentPublisher> envPublishers;
  @NonNull IdGenerator idGenerator;

  @Override
  public Mono<String> execute(final String applicationId,
                              final TaskType taskType,
                              final Integer replicas,
                              final Map<String, String> environment) {
    final var taskId = idGenerator.generate();

    final var env = this.updateEnvironment(environment, taskId, taskType);
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
    final var taskType = task.getType();

    final var command = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "down"))
        .environment(this.updateEnvironment(ImmutableMap.of(), taskId, taskType))
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = logsService.concat(commandService.execute(command));
      logsService.push(applicationId, taskId, LogType.TASK, logs);
      return taskId;
    });
  }

  @Override
  public Flux<FlatContainer> list() {
    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "-a",
            "--filter", "label=com.kraken.taskId",
            "--format", StringToFlatContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToFlatContainer);
  }

  @Override
  public Mono<Integer> hostsCount() {
    return Mono.just(1);
  }

  private Map<String, String> updateEnvironment(final Map<String, String> environment, final String taskId, final TaskType taskType) {
    // Update the env variable with all useful key/values
    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(environment);
    envBuilder.put("KRAKEN_TASK_ID", taskId);
    envBuilder.put("KRAKEN_EXPECTED_COUNT", dockerProperties.getContainersCount().get(taskType).toString());
    envPublishers.stream()
        .filter(environmentPublisher -> environmentPublisher.test(taskType))
        .forEach(environmentPublisher -> envBuilder.putAll(environmentPublisher.get()));

    return envBuilder.build();
  }

}
