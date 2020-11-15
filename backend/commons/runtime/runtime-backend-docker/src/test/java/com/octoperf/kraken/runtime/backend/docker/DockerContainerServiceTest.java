package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.command.executor.api.CommandService;
import com.octoperf.kraken.runtime.entity.log.LogType;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.logs.TaskLogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DockerContainerServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  TaskLogsService logsService;
  @Mock
  BiFunction<String, ContainerStatus, String> containerStatusToName;
  @Mock
  ContainerFindService findService;

  DockerContainerService service;

  @BeforeEach
  public void before() {
    this.service = new DockerContainerService(commandService, logsService, containerStatusToName, findService);
  }

  @Test
  public void shouldSetStatus() {
    final var containerId = "containerId";
    final var taskId = "taskId";
    final var status = ContainerStatus.RUNNING;
    final var containerName = "containerName";

    // Rename
    final var renameCommand = Command.builder()
        .path(".")
        .args(Arrays.asList("docker",
            "rename",
            containerId,
            containerName))
        .environment(ImmutableMap.of())
        .build();
    given(containerStatusToName.apply(containerName, status)).willReturn(containerName);
    given(findService.find(Owner.PUBLIC, taskId, containerName)).willReturn(Mono.just(FlatContainerTest.CONTAINER));
    final var renamed = Flux.just("renamed");
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0, Command.class)));
    given(commandService.execute(renameCommand)).willReturn(renamed);

    service.setStatus(Owner.PUBLIC, taskId, containerId, containerName, status).block();

    verify(commandService).execute(renameCommand);
  }

  @Test
  public void shouldAttachLogs() {
    final var containerName = "containerName";
    final var taskId = "taskId";
    final var containerId = "containerId";

    // Logs
    final var logsCommand = Command.builder()
        .path(".")
        .args(Arrays.asList("docker",
            "logs",
            "-f", containerId))
        .environment(ImmutableMap.of())
        .build();
    final var logs = Flux.just("logs");
    final var id = "taskId-containerId-containerName";
    given(commandService.validate(any(Command.class))).willAnswer(invocationOnMock -> Mono.just(invocationOnMock.getArgument(0, Command.class)));
    given(findService.find(Owner.PUBLIC, taskId, containerName)).willReturn(Mono.just(FlatContainerTest.CONTAINER));
    assertThat(service.attachLogs(Owner.PUBLIC, taskId, containerId, containerName).block()).isEqualTo(id);
    verify(logsService).push(eq(Owner.PUBLIC), eq(id), eq(LogType.CONTAINER), any());
  }

  @Test
  public void shouldDetachLogs() {
    service.detachLogs(Owner.PUBLIC, "id").block();
    verify(logsService).dispose(Owner.PUBLIC, "id", LogType.CONTAINER);
  }

  @Test
  public void shouldLogsId() {
    assertThat(service.logsId("task", "host", "container")).isEqualTo("task-host-container");
  }

  @Test
  public void shouldFind() {
    final var taskId = "taskId";
    final var containerName = "containerName";
    final var owner = Owner.PUBLIC;
    final var flat = FlatContainerTest.CONTAINER;
    given(findService.find(owner, taskId, containerName)).willReturn(Mono.just(flat));
    final var result = service.find(owner, taskId, containerName).block();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(flat);
  }
}
