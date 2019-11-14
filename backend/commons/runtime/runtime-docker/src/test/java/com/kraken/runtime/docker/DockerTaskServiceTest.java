package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
import com.kraken.runtime.docker.properties.DockerPropertiesTest;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.unique.id.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DockerTaskServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  LogsService logsService;
  @Mock
  Function<String, FlatContainer> stringToFlatContainer;
  @Mock
  Function<TaskType, String> taskTypeToPath;
  @Mock
  EnvironmentChecker envChecker;
  @Mock
  EnvironmentPublisher envPublisher;
  @Mock
  IdGenerator idGenerator;

  DockerTaskService service;

  @Before
  public void before() {
    service = new DockerTaskService(
        commandService,
        DockerPropertiesTest.DOCKER_PROPERTIES,
        logsService,
        stringToFlatContainer,
        taskTypeToPath,
        ImmutableList.of(envChecker),
        ImmutableList.of(envPublisher),
        idGenerator);
  }


  @Test
  public void shouldCountHosts() {
    assertThat(service.hostsCount().block()).isEqualTo(1);
  }

  @Test
  public void shouldExecute() {
    final var applicationId = "applicationId";
    final var taskId = "taskId";
    final var taskType = TaskType.RUN;
    final var replicas = 1;
    final var path = "path";
    final var env = ImmutableMap.of("KRAKEN_DESCRIPTION", "description");

    given(idGenerator.generate()).willReturn(taskId);
    given(envChecker.test(taskType)).willReturn(true);
    given(envPublisher.test(taskType)).willReturn(true);
    given(envPublisher.get()).willReturn(ImmutableMap.of("FOO", "BAR"));
    given(taskTypeToPath.apply(taskType)).willReturn(path);

    final var executeCmd = Command.builder()
        .path(path)
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "up",
            "-d",
            "--no-color"))
        .environment(ImmutableMap.<String, String>builder().putAll(env)
            .put("KRAKEN_TASK_ID", taskId)
            .put("KRAKEN_EXPECTED_COUNT", DockerPropertiesTest.DOCKER_PROPERTIES.getContainersCount().get(taskType).toString())
            .put("FOO", "BAR")
            .build())
        .build();

    final var logs = Flux.just("logs");
    given(commandService.execute(executeCmd)).willReturn(logs);
    given(logsService.concat(logs)).willReturn(logs);

    assertThat(service.execute(applicationId, taskType, replicas, env).block()).isEqualTo(taskId);

    verify(commandService).execute(executeCmd);
    verify(logsService).push(applicationId, taskId, LogType.TASK, logs);
  }

  @Test
  public void shouldCancel() {
    final var applicationId = "applicationId";
    final var taskId = TaskTest.TASK.getId();
    final var taskType = TaskType.RUN;
    final var path = "path";

    given(envPublisher.test(taskType)).willReturn(true);
    given(envPublisher.get()).willReturn(ImmutableMap.of("FOO", "BAR"));
    given(taskTypeToPath.apply(taskType)).willReturn(path);

    final var cancelCmd = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "down"))
        .environment(ImmutableMap.<String, String>builder()
            .put("KRAKEN_TASK_ID", taskId)
            .put("KRAKEN_EXPECTED_COUNT", DockerPropertiesTest.DOCKER_PROPERTIES.getContainersCount().get(taskType).toString())
            .put("FOO", "BAR")
            .build())
        .build();

    final var logs = Flux.just("logs");
    given(commandService.execute(cancelCmd)).willReturn(logs);
    given(logsService.concat(logs)).willReturn(logs);

    assertThat(service.cancel(applicationId, taskId, taskType).block()).isEqualTo(taskId);

    verify(commandService).execute(cancelCmd);
    verify(logsService).push(applicationId, taskId, LogType.TASK, logs);
  }

  @Test
  public void shouldList() {
    final var listCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "-a",
            "--filter", "label=com.kraken.taskId",
            "--format", StringToFlatContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    final var taskAsString = "taskAsString";
    given(commandService.execute(listCommand)).willReturn(Flux.just(taskAsString));
    given(stringToFlatContainer.apply(taskAsString)).willReturn(FlatContainerTest.CONTAINER);

    assertThat(service.list().blockLast()).isEqualTo(FlatContainerTest.CONTAINER);

    verify(commandService).execute(listCommand);
  }

}
