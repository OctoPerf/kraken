package com.kraken.commons.docker.client.spotify;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.executor.CommandExecutor;
import com.kraken.commons.docker.client.entity.DockerContainer;
import com.kraken.commons.docker.client.entity.DockerImage;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.Image;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static com.spotify.docker.client.DockerClient.LogsParam.stderr;
import static com.spotify.docker.client.DockerClient.LogsParam.stdout;
import static com.spotify.docker.client.DockerClient.RemoveContainerParam.forceKill;
import static com.spotify.docker.client.DockerClient.RemoveContainerParam.removeVolumes;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Mono.fromCallable;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class DockerSpotifyService implements DockerService {

  @NonNull
  DockerClient client;
  @NonNull
  CommandExecutor executor;
  @NonNull
  Function<String, ContainerConfig> ymlToConfig;

  @Override
  public Mono<String> pull(final String applicationId, final String image) {
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "pull", image))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return executor.execute(command);
  }

  @Override
  public Flux<DockerContainer> ps() {
    try {
      final var list = client.listContainers(allContainers());
      return Flux.fromIterable(list).map((Container container) -> DockerContainer.builder()
          .id(container.id())
          .name(container.names().stream().collect(Collectors.joining(", ")))
          .image(container.image())
          .status(container.status())
          .full(container)
          .build());
    } catch (DockerException | InterruptedException e) {
      return Flux.error(e);
    }
  }

  @Override
  public Mono<String> run(final String name, final String config) {
    return fromCallable(() -> {
      final var configuration = this.ymlToConfig.apply(config);
      final var id = client.createContainer(configuration, name).id();
      client.startContainer(id);
      return id;
    });
  }

  @Override
  public Mono<String> logs(final String applicationId, final String containerId) {
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(ImmutableList.of("docker", "logs", "-f", containerId))
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return executor.execute(command);
  }

  @Override
  public Mono<Boolean> start(final String containerId) {
    return fromCallable(() -> {
      client.startContainer(containerId);
      return true;
    });
  }

  @Override
  public Mono<Boolean> stop(final String containerId, final int secondsToWaitBeforeKilling) {
    return fromCallable(() -> {
      client.stopContainer(containerId, secondsToWaitBeforeKilling);
      return true;
    });
  }

  @Override
  public Mono<String> tail(final String containerId, final Integer lines) {
    return fromCallable(() -> client.logs(containerId, stdout(), stderr(), DockerClient.LogsParam.tail(lines)).readFully());
  }

  @Override
  public Mono<DockerContainer> inspect(final String containerId) {
    return fromCallable(() -> client.inspectContainer(containerId)).map((ContainerInfo info) -> DockerContainer.builder()
        .id(info.id())
        .name(info.name())
        .image(info.image())
        .status(info.state().status())
        .full(info)
        .build());
  }

  @Override
  public Mono<Boolean> rm(final String containerId) {
    return fromCallable(() -> {
      client.removeContainer(containerId, forceKill(), removeVolumes());
      return true;
    });
  }

  @Override
  public Flux<DockerImage> images() {
    try {
      final var list = client.listImages();
      return Flux.fromIterable(list).map((Image image) -> {
        final var repoDigest = Optional.ofNullable(image.repoDigests()).orElse(of())
            .stream().findFirst().orElse("<none>@<none>").split("@");
        final var repoTag = Optional.ofNullable(image.repoTags()).orElse(of())
            .stream().findFirst().orElse("<none>:<none>").split(":");
        return DockerImage.builder()
            .id(image.id())
            .name(repoDigest[0].equals("<none>") ? repoTag[0] : repoDigest[0])
            .tag(repoTag[1])
            .created(image.created())
            .size(image.size())
            .full(image)
            .build();
      });
    } catch (DockerException | InterruptedException e) {
      return Flux.error(e);
    }
  }

  @Override
  public Mono<Boolean> rmi(final String imageId) {
    return fromCallable(() -> {
      client.removeImage(imageId, true, false);
      return true;
    });
  }

  @Override
  public Mono<String> prune(final String applicationId, final boolean all, final boolean volumes) {
    final var builder = ImmutableList.<String>builder()
        .add("docker")
        .add("system")
        .add("prune")
        .add("-f");
    if (all) {
      builder.add("--all");
    }
    if (volumes) {
      builder.add("--volumes");
    }
    final Command command = Command.builder()
        .id("")
        .applicationId(applicationId)
        .command(builder.build())
        .environment(ImmutableMap.of())
        .path("")
        .onCancel(ImmutableList.of())
        .build();
    return executor.execute(command);
  }
}
