package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.backend.docker.DockerContainerService;
import com.kraken.runtime.backend.docker.StringToFlatContainer;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
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
  BiFunction<String, ContainerStatus, String> containerStatusToName;
  @Mock
  Function<String, FlatContainer> stringToFlatContainer;

  DockerContainerService service;

  @Before
  public void before() {
    this.service = new DockerContainerService(commandService, logsService, containerStatusToName, stringToFlatContainer);
  }

  @Test
  public void shouldSetStatus() {
    final var containerId = "containerId";
    final var hostname = "hostname";
    final var taskId = "taskId";
    final var status = ContainerStatus.RUNNING;
    final var containerName = "containerName";

    // Rename
    final var renameCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "rename",
            hostname,
            containerName))
        .environment(ImmutableMap.of())
        .build();
    given(containerStatusToName.apply(containerId, status)).willReturn(containerName);
    final var renamed = Flux.just("renamed");
    given(commandService.execute(renameCommand)).willReturn(renamed);

    service.setStatus(taskId, hostname, containerId, status).block();

    verify(commandService).execute(renameCommand);
  }

  @Test
  public void shouldAttachLogs() {
    final var applicationId = "applicationId";
    final var containerName = "containerName";
    final var taskId = "taskId";
    final var containerId = "containerId";

    // Logs
    final var logsCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "logs",
            "-f", containerId))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    final var id = "taskId-containerId-containerName";
    given(commandService.execute(logsCommand)).willReturn(logs);
    assertThat(service.attachLogs(applicationId, taskId, containerId, containerName).block()).isEqualTo(id);
    verify(logsService).push(applicationId, id, LogType.CONTAINER, logs);
  }

  @Test
  public void shouldDetachLogs() {
    service.detachLogs("appId", "id").block();
    verify(logsService).dispose("appId", "id", LogType.CONTAINER);
  }

  @Test
  public void shouldLogsId() {
    assertThat(service.logsId("task", "host", "container")).isEqualTo("task-host-container");
  }

  @Test
  public void shouldFind() {
    final var container = FlatContainerTest.CONTAINER;

    final var command = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "--filter", "label=com.kraken/taskId=" + container.getTaskId(),
            "--filter", "label=com.kraken/containerName=" + container.getName(),
            "--format", StringToFlatContainer.FORMAT,
            "--latest"))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(command)).willReturn(logs);
    given(stringToFlatContainer.apply("logs")).willReturn(container);
    final var found = service.find(container.getTaskId(), container.getName()).block();
    assertThat(found).isEqualTo(container);
  }
}
