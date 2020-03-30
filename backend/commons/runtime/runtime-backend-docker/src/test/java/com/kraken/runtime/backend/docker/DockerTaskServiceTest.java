package com.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.command.CommandService;
import com.kraken.runtime.context.entity.CancelContextTest;
import com.kraken.runtime.context.entity.ExecutionContext;
import com.kraken.runtime.context.entity.ExecutionContextTest;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.logs.LogsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static com.kraken.runtime.backend.api.EnvironmentLabels.COM_KRAKEN_TASKID;
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
  @Captor
  ArgumentCaptor<Command> commandCaptor;


  DockerTaskService service;

  @Before
  public void before() throws IOException {
    service = new DockerTaskService(
        commandService,
        logsService,
        stringToFlatContainer);

    FileSystemUtils.deleteRecursively(Paths.get("testDir/taskId"));
    FileSystemUtils.deleteRecursively(Paths.get("testDir/id"));
  }

  @Test
  public void shouldExecute() {
    final var context = ExecutionContextTest.EXECUTION_CONTEXT;
    final var logs = Flux.just("logs");
    given(commandService.execute(any())).willReturn(logs);

    assertThat(service.execute(context).block()).isEqualTo(context);

    verify(commandService).execute(commandCaptor.capture());
    verify(logsService).push(eq(context.getApplicationId()), eq(context.getTaskId()), eq(LogType.TASK), any());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("docker-compose",
        "--no-ansi",
        "up",
        "-d",
        "--no-color"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldExecuteFail() {
    final var context = ExecutionContext.builder()
        .applicationId("applicationId")
        .taskId("taskId")
        .taskType(TaskType.GATLING_RUN)
        .templates(ImmutableMap.of())
        .description("description")
        .build();

    service.execute(context).block();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldExecuteFailTooManyHosts() {
    final var context = ExecutionContext.builder()
        .applicationId("applicationId")
        .taskId("taskId")
        .taskType(TaskType.GATLING_RUN)
        .templates(ImmutableMap.of("hostId", "tpl", "other", "tpl"))
        .description("description")
        .build();

    service.execute(context).block();
  }

  @Test
  public void shouldCancel() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var logs = Flux.just("logs");
    given(commandService.execute(any())).willReturn(logs);
    assertThat(service.cancel(context).block()).isEqualTo(context);
    verify(commandService).execute(commandCaptor.capture());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q -f label=%s=%s)", COM_KRAKEN_TASKID, context.getTaskId())));
  }

  @Test
  public void shouldRemove() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var logs = Flux.just("logs");
    given(commandService.execute(any())).willReturn(logs);
    assertThat(service.remove(context).block()).isEqualTo(context);
    verify(commandService).execute(commandCaptor.capture());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q -f label=%s=%s)", COM_KRAKEN_TASKID, context.getTaskId())));
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
