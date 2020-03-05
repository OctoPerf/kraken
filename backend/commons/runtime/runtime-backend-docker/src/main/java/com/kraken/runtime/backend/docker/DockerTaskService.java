package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.runtime.context.entity.ExecutionContext;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.properties.ApplicationProperties;
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

  @Override
  public Mono<ExecutionContext> execute(final ExecutionContext context) {
    checkArgument(context.getTemplates().entrySet().size() <= 1, "The Docker runtime server can only run tasks on one host!");
    final var templateOptional = context.getTemplates().values().stream().findFirst();
    checkArgument(templateOptional.isPresent(), "The Docker runtime server can only run tasks on one host!");
    final var template = templateOptional.get();

    return Mono.fromCallable(() -> {
      final var path = this.createDockerComposeFolder(context.getTaskId(), template);
      final var command = Command.builder()
          .path(path)
          .command(Arrays.asList("docker-compose",
              "--no-ansi",
              "up",
              "-d",
              "--no-color"))
          .environment(ImmutableMap.of())
          .build();

      // Automatically display logs stream
      final var logs = commandService.execute(command).doOnTerminate(() -> this.removeDockerComposeFolder(context.getTaskId()));
      logsService.push(context.getApplicationId(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<CancelContext> cancel(final CancelContext context) {
    return Mono.fromCallable(() -> {
      final var path = this.createDockerComposeFolder(context.getTaskId(), context.getTemplate());
      final var command = Command.builder()
          .path(path)
          .command(Arrays.asList("docker-compose",
              "--no-ansi",
              "down"))
          .environment(ImmutableMap.of())
          .build();
      final var logs = commandService.execute(command).doOnTerminate(() -> this.removeDockerComposeFolder(context.getTaskId()));
      logsService.push(context.getApplicationId(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<CancelContext> remove(final CancelContext context) {
    final var command = Command.builder()
        .path(this.applicationProperties.getData().toString())
        .command(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q -f label=%s=%s)", COM_KRAKEN_TASK_ID, context.getTaskId())))
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

  private String createDockerComposeFolder(final String taskId, final String template) throws IOException {
    final var taskPath = Files.createDirectory(this.applicationProperties.getData().resolve(taskId));
    Files.writeString(taskPath.resolve(DOCKER_COMPOSE_YML), template);
    return taskPath.toString();
  }

  private void removeDockerComposeFolder(final String taskId) {
    final var taskPath = this.applicationProperties.getData().resolve(taskId);
    try {
      FileSystemUtils.deleteRecursively(taskPath);
    } catch (IOException e) {
      log.debug("Failed to remove task folder " + taskPath, e);
    }
  }

}
