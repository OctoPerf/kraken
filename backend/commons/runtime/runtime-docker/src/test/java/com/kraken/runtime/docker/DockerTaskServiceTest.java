package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.logs.LogsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

  DockerTaskService service;

  @Before
  public void before() {
    service = new DockerTaskService(
        commandService,
        logsService,
        stringToFlatContainer,
        taskTypeToPath,
        ImmutableList.of(envChecker),
        ImmutableList.of(envPublisher));
  }

  @Test
  public void shouldExecute() {
    final var applicationId = "applicationId";
    final var taskId = "taskId";
    final var taskType = TaskType.RUN;
    final var path = "path";

    final var context = ExecutionContext.builder()
        .taskType(TaskType.RUN)
        .taskId(taskId)
        .applicationId(applicationId)
        .description("description")
        .environment(ImmutableMap.of("foo", "bar"))
        .hosts(ImmutableMap.of())
        .build();

    given(envChecker.test(taskType)).willReturn(true);
    given(envPublisher.test(taskType)).willReturn(true);
    given(envPublisher.apply(any())).willReturn(ImmutableMap.of("FOO", "BAR"));
    given(taskTypeToPath.apply(taskType)).willReturn(path);

    final var executeCmd = Command.builder()
        .path(path)
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "up",
            "-d",
            "--no-color"))
        .environment(ImmutableMap.<String, String>builder().putAll(context.getEnvironment())
            .put("FOO", "BAR")
            .build())
        .build();

    final var logs = Flux.just("logs");
    given(commandService.execute(executeCmd)).willReturn(logs);

    assertThat(service.execute(context).block()).isEqualTo(context);

    verify(commandService).execute(executeCmd);
    verify(logsService).push(applicationId, taskId, LogType.TASK, logs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldExecuteFail() {
    final var context = ExecutionContext.builder()
        .taskType(TaskType.RUN)
        .taskId("taskId")
        .applicationId("applicationId")
        .description("description")
        .environment(ImmutableMap.of("foo", "bar"))
        .hosts(ImmutableMap.of("host1", ImmutableMap.of(), "host2", ImmutableMap.of()))
        .build();
    service.execute(context).block();
  }

  @Test
  public void shouldCancel() {
    final var applicationId = "applicationId";
    final var taskId = TaskTest.TASK.getId();
    final var taskType = TaskType.RUN;
    final var path = "path";

    given(envPublisher.test(taskType)).willReturn(true);
    given(envPublisher.apply(any())).willReturn(ImmutableMap.of("FOO", "BAR"));
    given(taskTypeToPath.apply(taskType)).willReturn(path);

    final var cancelCmd = Command.builder()
        .path(taskTypeToPath.apply(taskType))
        .command(Arrays.asList("docker-compose",
            "--no-ansi",
            "down"))
        .environment(ImmutableMap.<String, String>builder()
            .put("FOO", "BAR")
            .build())
        .build();

    final var logs = Flux.just("logs");
    given(commandService.execute(cancelCmd)).willReturn(logs);

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
            "--filter", "label=com.kraken/taskId",
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
