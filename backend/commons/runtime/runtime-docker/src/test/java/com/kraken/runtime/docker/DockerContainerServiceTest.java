package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.*;
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
  Function<String, FlatContainer> stringToFlatContainer;
  @Mock
  BiFunction<String, ContainerStatus, String> containerStatusToName;

  DockerContainerService service;

  @Before
  public void before() {
    this.service = new DockerContainerService(commandService, logsService, stringToFlatContainer, containerStatusToName);
  }

  @Test
  public void shouldSetStatus() {
    final var flatContainer = FlatContainerTest.CONTAINER;
    final var status = ContainerStatus.RUNNING;

    // Find
    final var findCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + flatContainer.getContainerId(),
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    given(commandService.execute(findCommand)).willReturn(Flux.just("found"));
    given(stringToFlatContainer.apply("found")).willReturn(flatContainer);

    // Rename
    final var containerName = "containerName";
    final var renameCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "rename",
            flatContainer.getId(),
            containerName))
        .environment(ImmutableMap.of())
        .build();
    given(containerStatusToName.apply(flatContainer.getContainerId(), status)).willReturn(containerName);
    final var renamed = Flux.just("renamed");
    given(commandService.execute(renameCommand)).willReturn(renamed);

    service.setStatus(flatContainer.getTaskId(), flatContainer.getHostId(), flatContainer.getContainerId(), status).block();

    verify(commandService).execute(renameCommand);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "applicationId";
    final var container = ContainerTest.CONTAINER;
    final var flatContainer = FlatContainerTest.CONTAINER;

    // Find
    final var findCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken.containerId=" + flatContainer.getContainerId(),
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    given(commandService.execute(findCommand)).willReturn(Flux.just("found"));
    given(stringToFlatContainer.apply("found")).willReturn(flatContainer);

    // Logs
    final var logsCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "logs",
            "-f", container.getId()))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(logsCommand)).willReturn(logs);

    service.attachLogs(applicationId, flatContainer.getTaskId(), flatContainer.getHostId(), flatContainer.getContainerId()).block();

    verify(logsService).push(applicationId, service.logsId(flatContainer.getTaskId(), flatContainer.getHostId(), flatContainer.getContainerId()), LogType.CONTAINER, logs);
  }

  @Test
  public void shouldDetachLogs() {
    service.detachLogs("id").block();
    verify(logsService).cancel("id");
  }

  @Test
  public void shouldLogsId() {
    assertThat(service.logsId("task", "host", "container")).isEqualTo("task-host-container");
  }
}
