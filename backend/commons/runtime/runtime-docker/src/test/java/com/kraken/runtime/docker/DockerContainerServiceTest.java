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
  BiFunction<String, ContainerStatus, String> containerStatusToName;

  DockerContainerService service;

  @Before
  public void before() {
    this.service = new DockerContainerService(commandService, logsService, containerStatusToName);
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
    final var hostname = "hostname";
    final var taskId = "taskId";
    final var containerId = "containerId";

    // Logs
    final var logsCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "logs",
            "-f", hostname))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    given(commandService.execute(logsCommand)).willReturn(logs);

    service.attachLogs(applicationId, taskId, hostname, containerId).block();

    verify(logsService).push(applicationId, service.logsId(taskId, hostname, containerId), LogType.CONTAINER, logs);
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
