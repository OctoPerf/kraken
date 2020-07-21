package com.octoperf.kraken.runtime.backend.docker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.runtime.command.CommandService;
import com.octoperf.kraken.runtime.context.entity.CancelContextTest;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextTest;
import com.octoperf.kraken.runtime.entity.log.LogType;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.security.entity.owner.ApplicationOwner;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.octoperf.kraken.runtime.backend.api.EnvironmentLabels.COM_OCTOPERF_TASKID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DockerTaskServiceTest {

  @Mock
  CommandService commandService;
  @Mock
  LogsService logsService;
  @Mock
  Function<String, FlatContainer> stringToFlatContainer;
  @Mock(lenient = true)
  Function<Owner, List<String>> ownerToFilters;

  @Captor
  ArgumentCaptor<Command> commandCaptor;

  DockerTaskService service;

  @BeforeEach
  public void before() throws IOException {
    service = new DockerTaskService(
        commandService,
        logsService,
        stringToFlatContainer,
        ownerToFilters);
    given(ownerToFilters.apply(any())).willReturn(ImmutableList.of("--filter", "ownerToFilter"));
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
    verify(logsService).push(eq(context.getOwner()), eq(context.getTaskId()), eq(LogType.TASK), any());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("docker-compose",
        "--no-ansi",
        "up",
        "-d",
        "--no-color"));
  }

  @Test
  public void shouldExecuteFail() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      final var context = ExecutionContext.builder()
          .owner(ApplicationOwner.builder().applicationId("applicationId").build())
          .taskId("taskId")
          .taskType(TaskType.GATLING_RUN)
          .templates(ImmutableMap.of())
          .description("description")
          .build();

      service.execute(context).block();
    });
  }

  @Test
  public void shouldExecuteFailTooManyHosts() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      final var context = ExecutionContext.builder()
          .owner(ApplicationOwner.builder().applicationId("applicationId").build())
          .taskId("taskId")
          .taskType(TaskType.GATLING_RUN)
          .templates(ImmutableMap.of("hostId", "tpl", "other", "tpl"))
          .description("description")
          .build();

      service.execute(context).block();
    });
  }

  @Test
  public void shouldCancel() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var logs = Flux.just("logs");
    given(commandService.execute(any())).willReturn(logs);
    assertThat(service.cancel(context).block()).isEqualTo(context);
    verify(commandService).execute(commandCaptor.capture());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q --filter label=%s=%s --filter ownerToFilter)", COM_OCTOPERF_TASKID, context.getTaskId())));
  }

  @Test
  public void shouldRemove() {
    final var context = CancelContextTest.CANCEL_CONTEXT;
    final var logs = Flux.just("logs");
    given(commandService.execute(any())).willReturn(logs);
    assertThat(service.remove(context).block()).isEqualTo(context);
    verify(commandService).execute(commandCaptor.capture());

    final var executed = commandCaptor.getValue();
    assertThat(executed.getCommand()).isEqualTo(Arrays.asList("/bin/sh", "-c", String.format("docker rm -v -f $(docker ps -a -q --filter label=%s=%s --filter ownerToFilter)", COM_OCTOPERF_TASKID, context.getTaskId())));
  }

  @Test
  public void shouldList() {
    final var listCommand = Command.builder()
        .path(".")
        .command(Arrays.asList("docker",
            "ps",
            "-a",
            "--filter", "label=com.octoperf/taskId",
            "--format", StringToFlatContainer.FORMAT))
        .environment(ImmutableMap.of())
        .build();

    final var taskAsString = "taskAsString";
    given(ownerToFilters.apply(PublicOwner.INSTANCE)).willReturn(ImmutableList.of());
    given(commandService.execute(listCommand)).willReturn(Flux.just(taskAsString));
    given(stringToFlatContainer.apply(taskAsString)).willReturn(FlatContainerTest.CONTAINER);

    assertThat(service.list(PublicOwner.INSTANCE).blockLast()).isEqualTo(FlatContainerTest.CONTAINER);

    verify(commandService).execute(listCommand);
  }

}
