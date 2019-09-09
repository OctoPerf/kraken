package com.kraken.runtime.server.service;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.storage.entity.StorageNodeTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResultUpdaterTest {

  @Mock
  TaskService taskService;

  @Mock
  AnalysisClient analysisClient;

  SpringResultUpdater updater;

  @Before
  public void before() {
    this.updater = new SpringResultUpdater(taskService, analysisClient, new TaskTypeToResultType(), new TaskStatusToResultStatus());
  }

  @Test
  public void shouldHandleTaskExecuted() {
    final var taskId= "taskId";
    final var taskType = TaskType.RECORD;
    final var description = "description";

    given(analysisClient.create(any(Result.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    assertThat(updater.taskExecuted(taskId, taskType, description).block()).isEqualTo(taskId);

    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
    verify(analysisClient).create(resultCaptor.capture());

    final var result = resultCaptor.getValue();
    assertThat(result.getId()).isEqualTo(taskId);
    assertThat(result.getStartDate()).isLessThanOrEqualTo(new Date().getTime());
    assertThat(result.getEndDate()).isEqualTo(0L);
    assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
    assertThat(result.getType()).isEqualTo(ResultType.HAR);
  }

  @Test
  public void shouldHandleTaskCancelled() {
    final var taskId= "taskId";
    given(analysisClient.setStatus(taskId, ResultStatus.CANCELED)).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
    assertThat(updater.taskCanceled(taskId).block()).isEqualTo(taskId);
    verify(analysisClient).setStatus(taskId, ResultStatus.CANCELED);
  }

  @Test
  public void shouldUpdateResults() {
    final var taskId= "taskId";

    final var task0 = TaskTest.TASK;
    final var task10 = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(ContainerStatus.CREATING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .build();
    final var task11 = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(ContainerStatus.RUNNING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .build();
    final var task12 = Task.builder()
        .id(taskId)
        .startDate(42L)
        .status(ContainerStatus.DONE)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .build();

    given(taskService.watch()).willReturn(Flux.just(ImmutableList.of(task0, task10), ImmutableList.of(task0, task11), ImmutableList.of(task0, task12)));
    given(analysisClient.setStatus(anyString(), any(ResultStatus.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));

    updater.start();

    verify(analysisClient, times(3)).setStatus(TaskTest.TASK.getId(), ResultStatus.STARTING);
    verify(analysisClient).setStatus(taskId, ResultStatus.STARTING);
    verify(analysisClient).setStatus(taskId, ResultStatus.RUNNING);
    verify(analysisClient).setStatus(taskId, ResultStatus.COMPLETED);
  }
}
