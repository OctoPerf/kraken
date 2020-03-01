package com.kraken.runtime.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.docker.env.EnvironmentChecker;
import com.kraken.runtime.docker.env.EnvironmentPublisher;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.logs.LogsService;
import com.kraken.tools.properties.ApplicationPropertiesTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static com.kraken.tools.environment.KrakenEnvironmentLabels.COM_KRAKEN_TASK_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
  EnvironmentChecker envChecker;
  @Mock
  EnvironmentPublisher envPublisher;

  DockerTaskService service;

  @Before
  public void before() throws IOException {
    service = new DockerTaskService(
        commandService,
        logsService,
        stringToFlatContainer,
        ApplicationPropertiesTest.APPLICATION_PROPERTIES,
        ImmutableList.of(envChecker),
        ImmutableList.of(envPublisher));

    FileSystemUtils.deleteRecursively(Paths.get("testDir/taskId"));
    FileSystemUtils.deleteRecursively(Paths.get("testDir/id"));
  }

  @Test
  public void shouldExecute() {
    final var applicationId = "applicationId";
    final var taskId = "taskId";
    final var taskType = TaskType.RUN;

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

    final var executeCmd = Command.builder()
        .path("testDir/taskId")
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
    verify(logsService).push(eq(applicationId), eq(taskId), eq(LogType.TASK), any());
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

    given(envPublisher.test(taskType)).willReturn(true);
    given(envPublisher.apply(any())).willReturn(ImmutableMap.of("FOO", "BAR"));

    final var cancelCmd = Command.builder()
        .path("testDir/id")
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
    verify(logsService).push(eq(applicationId), eq(taskId), eq(LogType.TASK), any());
  }

  @Test
  public void shouldRemove() {
    final var applicationId = "applicationId";
    final var taskId = TaskTest.TASK.getId();
    final var taskType = TaskType.RUN;

    final var removeCmd = Command.builder()
        .path("testDir")
        .command(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q -f label=%s=%s)", COM_KRAKEN_TASK_ID, taskId)))
        .environment(ImmutableMap.of())
        .build();

    final var logs = Flux.just("logs");
    given(commandService.execute(removeCmd)).willReturn(logs);

    assertThat(service.remove(applicationId, taskId, taskType).block()).isEqualTo(taskId);

    verify(commandService).execute(removeCmd);
  }

  @Test
  public void shouldList() {
    final var listCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "-a",
            "--filter", "label=com.kraken/taskId",
            "--filter", "label=com.kraken/applicationId=app",
            "--format", StringToFlatContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    final var taskAsString = "taskAsString";
    given(commandService.execute(listCommand)).willReturn(Flux.just(taskAsString));
    given(stringToFlatContainer.apply(taskAsString)).willReturn(FlatContainerTest.CONTAINER);

    assertThat(service.list(Optional.of("app")).blockLast()).isEqualTo(FlatContainerTest.CONTAINER);

    verify(commandService).execute(listCommand);
  }

  @Test
  public void shouldListNoApp() {
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

    assertThat(service.list(Optional.empty()).blockLast()).isEqualTo(FlatContainerTest.CONTAINER);

    verify(commandService).execute(listCommand);
  }

}
