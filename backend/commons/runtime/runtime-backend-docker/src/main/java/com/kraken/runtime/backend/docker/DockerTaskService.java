package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.properties.ApplicationProperties;
import com.runtime.context.api.ExecutionContextService;
import com.runtime.context.entity.CancelContext;
import com.runtime.context.entity.ExecutionContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.kraken.tools.environment.KrakenEnvironmentLabels.COM_KRAKEN_APPLICATION_ID;
import static com.kraken.tools.environment.KrakenEnvironmentLabels.COM_KRAKEN_TASK_ID;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerTaskService implements TaskService {

  public static final String DOCKER_COMPOSE_YML = "docker-compose.yml";
  @NonNull CommandService commandService;
  @NonNull LogsService logsService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull ApplicationProperties applicationProperties;
  @NonNull ExecutionContextService contextService;

  @Override
  public Mono<ExecutionContext> execute(final ExecutionContext context) {
    final var hostContexts = contextService.toHostExecutionContexts(context);
    checkArgument(hostContexts.size() == 1, "The Docker runtime server can only run tasks on one host!");
    final var hostContext = hostContexts.get(0);

    return Mono.fromCallable(() -> {
      final var path = this.createDockerComposeFolder(context);
      final var command = Command.builder()
          .path(path)
          .command(Arrays.asList("docker-compose",
              "--no-ansi",
              "up",
              "-d",
              "--no-color"))
          .environment(hostContext.getEnvironment())
          .build();

      // Automatically display logs stream
      final var logs = commandService.execute(command).doOnTerminate(() -> this.removeDockerComposeFolder(context));
      logsService.push(context.getApplicationId(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<CancelContext> cancel(final CancelContext context) {
    return Mono.fromCallable(() -> {
      final var path = this.createDockerComposeFolder(context);
      final var command = Command.builder()
          .path(path)
          .command(Arrays.asList("docker-compose",
              "--no-ansi",
              "down"))
          .environment(this.updateEnvironment(context))
          .build();
      final var logs = commandService.execute(command).doOnTerminate(() -> this.removeDockerComposeFolder(context));
      logsService.push(context.getApplicationId(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<ExecutionContext> remove(final ExecutionContext context) {
    final var command = Command.builder()
        .path(this.applicationProperties.getData().toString())
        .command(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q -f label=%s=%s)", COM_KRAKEN_TASK_ID, taskId)))
        .environment(ImmutableMap.of())
        .build();
    return commandService.execute(command).collectList().map(strings -> context);
  }

  @Override
  public Flux<FlatContainer> list(Optional<String> applicationId) {
    final var commandBuilder = ImmutableList.<String>builder();
    commandBuilder.add("docker",
        "ps",
        "-a",
        "--filter", String.format("label=%s", COM_KRAKEN_TASK_ID));
    applicationId.ifPresent(appId -> commandBuilder.add("--filter", String.format("label=%s=%s", COM_KRAKEN_APPLICATION_ID, appId)));
    commandBuilder.add("--format", StringToFlatContainer.FORMAT);

    final var command = Command.builder()
        .path(".")
        .command(commandBuilder.build())
        .environment(ImmutableMap.of())
        .build();

    return commandService.execute(command)
        .map(stringToFlatContainer);
  }

  private Map<String, String> updateEnvironment(final ExecutionContext context) {
    // Update the env variable with all useful key/values
    final var envBuilder = ImmutableMap.<String, String>builder();
    envBuilder.putAll(context.getEnvironment());
    envPublishers.stream()
        .filter(environmentPublisher -> environmentPublisher.test(context.getTaskType()))
        .forEach(environmentPublisher -> envBuilder.putAll(environmentPublisher.apply(context)));
    return envBuilder.build();
  }


  private String createDockerComposeFolder(final ExecutionContext context) throws IOException {
    final var taskPath = Files.createDirectory(this.applicationProperties.getData().resolve(context.getTaskId()));
    final var composePath = this.applicationProperties.getData().resolve(context.getTaskType().toString()).resolve(DOCKER_COMPOSE_YML);
    Files.copy(composePath, taskPath.resolve(DOCKER_COMPOSE_YML));
    return taskPath.toString();
  }

  private void removeDockerComposeFolder(final ExecutionContext context) {
    final var taskPath = this.applicationProperties.getData().resolve(context.getTaskId());
    try {
      FileSystemUtils.deleteRecursively(taskPath);
    } catch (IOException e) {
      log.debug("Failed to remove task folder " + taskPath, e);
    }
  }

}
