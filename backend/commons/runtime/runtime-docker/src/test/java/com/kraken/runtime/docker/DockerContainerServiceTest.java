package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.entity.DockerContainer;
import com.kraken.runtime.docker.entity.DockerContainerTest;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.ContainerTest;
import com.kraken.runtime.entity.LogType;
import com.kraken.runtime.logs.LogsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerContainerServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  LogsService logsService;
  @Mock
  Function<String, DockerContainer> stringToDockerContainer;
  @Mock
  BiFunction<String, ContainerStatus, String> containerStatusToName;
  @Mock
  Function<DockerContainer, Container> dockerContainerToContainer;

  DockerContainerService service;

  @Before
  public void before() {
    this.service = new DockerContainerService(commandService, logsService, stringToDockerContainer, containerStatusToName, dockerContainerToContainer);
  }

  @Test
  public void shouldSetStatus() {
    final var container = ContainerTest.CONTAINER;
    final var dockerContainer = DockerContainerTest.CONTAINER;
    final var status = ContainerStatus.RUNNING;

    // Find
    final var findCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + dockerContainer.getContainerId(),
            "--format", StringToDockerContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    given(commandService.execute(findCommand)).willReturn(Flux.just("found"));
    given(stringToDockerContainer.apply("found")).willReturn(dockerContainer);

    // Rename
    final var containerName = "containerName";
    final var renameCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "rename",
            dockerContainer.getId(),
            containerName))
        .environment(ImmutableMap.of())
        .build();
    given(containerStatusToName.apply(dockerContainer.getContainerId(), status)).willReturn(containerName);
    final var renamed = Flux.just("renamed");
    given(commandService.execute(renameCommand)).willReturn(renamed);
    given(logsService.concat(renamed)).willReturn(renamed);
    given(dockerContainerToContainer.apply(dockerContainer.withStatus(status))).willReturn(container);

    assertThat(service.setStatus(dockerContainer.getTaskId(), dockerContainer.getContainerId(), status).block()).isEqualTo(container);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "applicationId";
    final var container = ContainerTest.CONTAINER;
    final var dockerContainer = DockerContainerTest.CONTAINER;
    final var status = ContainerStatus.RUNNING;

    // Find
    final var findCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + dockerContainer.getContainerId(),
            "--format", StringToDockerContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    given(commandService.execute(findCommand)).willReturn(Flux.just("found"));
    given(stringToDockerContainer.apply("found")).willReturn(dockerContainer);

    // Logs
    final var containerName = "containerName";
    final var logsCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "logs",
            "-f", container.getId()))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(logsCommand)).willReturn(logs);
    given(logsService.concat(logs)).willReturn(logs);

    service.attachLogs(applicationId, dockerContainer.getTaskId(), dockerContainer.getContainerId()).block();

    verify(logsService).push(applicationId, dockerContainer.getContainerId(), LogType.CONTAINER, logs);
  }

  @Test
  public void shouldDetachLogs() {
    final var dockerContainer = DockerContainerTest.CONTAINER;
    service.detachLogs(dockerContainer.getTaskId(), dockerContainer.getContainerId()).block();
    verify(logsService).cancel(dockerContainer.getContainerId());
  }
}
