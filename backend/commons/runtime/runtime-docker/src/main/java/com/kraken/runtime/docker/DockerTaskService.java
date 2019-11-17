package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.entity.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.runtime.server.properties.RuntimeServerProperties;
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

import static com.google.common.base.Preconditions.checkArgument;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerTaskService implements TaskService {

  @NonNull CommandService commandService;
  @NonNull RuntimeServerProperties serverProperties;
  @NonNull LogsService logsService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull Function<TaskType, String> taskTypeToPath;
  @NonNull List<EnvironmentChecker> envCheckers;
  @NonNull List<EnvironmentPublisher> envPublishers;

  @Override
  public Mono<ExecutionContext> execute(final ExecutionContext context) {
    checkArgument(context.getHosts().isEmpty(), "The Docker runtime server can only run tasks on one host!");

    final var env = this.updateEnvironment(context.getEnvironment(), context.getTaskId(), context.getTaskType(), context.getDescription());
    envCheckers.stream()
        .filter(environmentChecker -> environmentChecker.test(context.getTaskType()))
        .forEach(environmentChecker -> environmentChecker.accept(env));

    final var command = Command.builder()
        .path(taskTypeToPath.apply(context.getTaskType()))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "up",
            "-d",
            "--no-color"))
        .environment(env)
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = commandService.execute(command);
      logsService.push(context.getApplicationId(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<String> cancel(final String applicationId, final String taskId, final TaskType taskType) {
    final var command = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "down"))
        .environment(this.updateEnvironment(ImmutableMap.of(), taskId, taskType, ""))
        .build();

    return Mono.fromCallable(() -> {
      // Automatically display logs stream
      final var logs = commandService.execute(command);
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
            "--filter", "label=com.kraken/taskId",
            "--format", StringToFlatContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToFlatContainer);
  }

  private Map<String, String> updateEnvironment(final Map<String, String> environment, final String taskId, final TaskType taskType, final String description) {
    // Update the env variable with all useful key/values
    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(environment);
    envBuilder.put("KRAKEN_TASK_ID", taskId);
    envBuilder.put("KRAKEN_DESCRIPTION", description);
    envBuilder.put("KRAKEN_EXPECTED_COUNT", serverProperties.getContainersCount().get(taskType).toString());
    envPublishers.stream()
        .filter(environmentPublisher -> environmentPublisher.test(taskType))
        .forEach(environmentPublisher -> envBuilder.putAll(environmentPublisher.get()));

    return envBuilder.build();
  }

}
