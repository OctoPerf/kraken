package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.backend.api.TaskService;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.entity.log.LogType;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
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
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.octoperf.kraken.runtime.backend.api.EnvironmentLabel.COM_OCTOPERF_TASKID;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerTaskService implements TaskService {

  public static final String DOCKER_COMPOSE_YML = "docker-compose.yml";
  @NonNull CommandService commandService;
  @NonNull TaskLogsService logsService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull Function<Owner, List<String>> ownerToFilters;

  @Override
  public Mono<ExecutionContext> execute(final ExecutionContext context) {
    checkArgument(context.getTemplates().entrySet().size() <= 1, "The Docker runtime server can only run tasks on one host!");
    final var templateOptional = context.getTemplates().values().stream().findFirst();
    checkArgument(templateOptional.isPresent(), "The Docker runtime server can only run tasks on one host!");
    final var template = templateOptional.get();

    return Mono.fromCallable(() -> {
      final var path = this.createDockerComposeFolder(context.getTaskId(), template);
      final var command = Command.builder()
          .path(path.toString())
          .args(Arrays.asList("docker-compose",
              "--no-ansi",
              "up",
              "-d",
              "--no-color"))
          .environment(ImmutableMap.of())
          .build();

      // Automatically display logs stream
      final var logs = commandService
          .validate(command)
          .flatMapMany(commandService::execute)
          .doOnTerminate(() -> this.removeDockerComposeFolder(path));
      logsService.push(context.getOwner(), context.getTaskId(), LogType.TASK, logs);
      return context;
    });
  }

  @Override
  public Mono<CancelContext> cancel(final CancelContext context) {
    return this.remove(context);
  }

  @Override
  public Mono<CancelContext> remove(final CancelContext context) {
    return Mono.fromCallable(() -> {
      final var listCommandBuilder = ImmutableList.<String>builder();
      listCommandBuilder.add("docker",
          "ps",
          "-a",
          "-q",
          "--filter", String.format("label=%s=%s", COM_OCTOPERF_TASKID, context.getTaskId()));
      listCommandBuilder.addAll(ownerToFilters.apply(context.getOwner()));
      final var listCommand = String.join(" ", listCommandBuilder.build());

      return Command.builder()
          .path(this.createCommandFolder(context.getTaskId()).toString())
          .args(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(%s)", listCommand)))
          .environment(ImmutableMap.of())
          .build();
    })
        .flatMap(command -> commandService.validate(command).flatMapMany(commandService::execute).collectList())
        .map(str -> context);
  }

  @Override
  public Flux<FlatContainer> list(final Owner owner) {
    final var commandBuilder = ImmutableList.<String>builder();
    commandBuilder.add("docker",
        "ps",
        "-a",
        "--filter", String.format("label=%s", COM_OCTOPERF_TASKID));
    commandBuilder.addAll(ownerToFilters.apply(owner));
    commandBuilder.add("--format", StringToFlatContainer.FORMAT);

    final var command = Command.builder()
        .path(".")
        .args(commandBuilder.build())
        .environment(ImmutableMap.of())
        .build();

    return commandService.validate(command)
        .flatMapMany(commandService::execute)
        .map(stringToFlatContainer);
  }

  private Path createCommandFolder(final String taskId) throws IOException {
    return Files.createTempDirectory(taskId);
  }

  private Path createDockerComposeFolder(final String taskId, final String template) throws IOException {
    final var taskPath = this.createCommandFolder(taskId);
    Files.writeString(taskPath.resolve(DOCKER_COMPOSE_YML), template);
    return taskPath;
  }

  private void removeDockerComposeFolder(final Path taskPath) {
    try {
      FileSystemUtils.deleteRecursively(taskPath);
    } catch (IOException e) {
      log.debug("Failed to remove task folder " + taskPath, e);
    }
  }

}
