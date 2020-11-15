package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.octoperf.kraken.runtime.backend.api.EnvironmentLabel.COM_OCTOPERF_CONTAINER_NAME;
import static com.octoperf.kraken.runtime.backend.api.EnvironmentLabel.COM_OCTOPERF_TASKID;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class DockerContainerFindService implements ContainerFindService {

  @NonNull CommandService commandService;
  @NonNull Function<String, FlatContainer> stringToFlatContainer;
  @NonNull Function<Owner, List<String>> ownerToFilters;

  @Override
  public Mono<FlatContainer> find(final Owner owner, final String taskId, final String containerName) {
    final var commandBuilder = ImmutableList.<String>builder();
    commandBuilder.add("docker",
        "ps",
        "--filter", String.format("label=%s=%s", COM_OCTOPERF_TASKID, taskId),
        "--filter", String.format("label=%s=%s", COM_OCTOPERF_CONTAINER_NAME, containerName));
    commandBuilder.addAll(ownerToFilters.apply(owner));
    commandBuilder.add("--format", StringToFlatContainer.FORMAT, "--latest");

    final var command = Command.builder()
        .path(".")
        .args(commandBuilder.build())
        .environment(ImmutableMap.of())
        .build();

    return commandService.validate(command)
        .flatMapMany(commandService::execute)
        .map(stringToFlatContainer)
        .next();
  }
}
