package com.kraken.runtime.server.event;

import com.google.common.collect.ImmutableList;
import com.kraken.analysis.client.AnalysisClient;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.*;
import com.kraken.runtime.event.TaskCreatedEvent;
import com.kraken.runtime.event.TaskRemovedEvent;
import com.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.kraken.runtime.server.service.TaskListService;
import com.kraken.storage.entity.StorageNodeTest;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskUpdateHandlerTest {

  @Mock
  TaskListService taskListService;

  @Mock
  EventBus eventBus;

  SpringTaskUpdateHandler updater;

  @Before
  public void before() {
    this.updater = new SpringTaskUpdateHandler(taskListService, eventBus);
  }

  @Test
  public void scanForUpdates() {
    final var tasksStep0 = ImmutableList.<Task>of();
    final var tasksStep1 = ImmutableList.of(Task.builder()
        .id("taskId")
        .startDate(42L)
        .status(ContainerStatus.CREATING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .applicationId("app")
        .build());
    final var tasksStep2 = ImmutableList.of(Task.builder()
        .id("taskId")
        .startDate(42L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .applicationId("app")
        .build());
    final var tasksStep3 = ImmutableList.<Task>of();

    given(taskListService.watch()).willReturn(Flux.just(tasksStep0, tasksStep1, tasksStep2, tasksStep3));

    updater.start();

    verify(eventBus).publish(TaskCreatedEvent.builder().task(tasksStep1.get(0)).build());
    verify(eventBus).publish(TaskStatusUpdatedEvent.builder().previousStatus(tasksStep1.get(0).getStatus()).task(tasksStep2.get(0)).build());
    verify(eventBus).publish(TaskRemovedEvent.builder().task(tasksStep2.get(0)).build());
  }

//  @Test
//  public void shouldHandleTaskExecuted() {
//    final var context = ExecutionContextTest.EXECUTION_CONTEXT;
//
//    given(analysisClient.create(any(Result.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
//
//    assertThat(updater.taskExecuted(context).block()).isEqualTo(context.getTaskId());
//
//    final ArgumentCaptor<Result> resultCaptor = ArgumentCaptor.forClass(Result.class);
//    verify(analysisClient).create(resultCaptor.capture());
//
//    final var result = resultCaptor.getValue();
//    assertThat(result.getId()).isEqualTo(context.getTaskId());
//    assertThat(result.getStartDate()).isLessThanOrEqualTo(new Date().getTime());
//    assertThat(result.getEndDate()).isEqualTo(0L);
//    assertThat(result.getStatus()).isEqualTo(ResultStatus.STARTING);
//    assertThat(result.getType()).isEqualTo(ResultType.RUN);
//  }
//
//  @Test
//  public void shouldHandleTaskCancelled() {
//    final var taskId = "taskId";
//    given(analysisClient.setStatus(taskId, ResultStatus.CANCELED)).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
//    assertThat(updater.taskCanceled(taskId).block()).isEqualTo(taskId);
//    verify(analysisClient).setStatus(taskId, ResultStatus.CANCELED);
//  }
//
//  @Test
//  public void shouldUpdateResults() {
//    final var taskId = "taskId";
//
//    final var task0 = TaskTest.TASK;
//    final var task10 = Task.builder()
//        .id(taskId)
//        .startDate(42L)
//        .status(ContainerStatus.CREATING)
//        .type(TaskType.RUN)
//        .containers(ImmutableList.of())
//        .description("description")
//        .expectedCount(2)
//        .build();
//    final var task11 = Task.builder()
//        .id(taskId)
//        .startDate(42L)
//        .status(ContainerStatus.RUNNING)
//        .type(TaskType.RUN)
//        .containers(ImmutableList.of())
//        .description("description")
//        .expectedCount(2)
//        .build();
//    final var task12 = Task.builder()
//        .id(taskId)
//        .startDate(42L)
//        .status(ContainerStatus.DONE)
//        .type(TaskType.RUN)
//        .containers(ImmutableList.of())
//        .description("description")
//        .expectedCount(2)
//        .build();
//
//    given(taskListService.watch()).willReturn(Flux.just(ImmutableList.of(task0, task10), ImmutableList.of(task0, task11), ImmutableList.of(task0, task12)));
//    given(analysisClient.setStatus(anyString(), any(ResultStatus.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
//
//    updater.start();
//
//    verify(analysisClient, times(3)).setStatus(TaskTest.TASK.getId(), ResultStatus.STARTING);
//    verify(analysisClient).setStatus(taskId, ResultStatus.STARTING);
//    verify(analysisClient).setStatus(taskId, ResultStatus.RUNNING);
//    verify(analysisClient).setStatus(taskId, ResultStatus.COMPLETED);
//  }
//
//
//  @Test
//  public void shouldUpdateResultsWithError() {
//    final var task0 = Task.builder()
//        .id("task0")
//        .startDate(42L)
//        .status(ContainerStatus.CREATING)
//        .type(TaskType.RUN)
//        .containers(ImmutableList.of())
//        .description("description")
//        .expectedCount(2)
//        .build();
//    final var task1 = Task.builder()
//        .id("task1")
//        .startDate(42L)
//        .status(ContainerStatus.RUNNING)
//        .type(TaskType.RUN)
//        .containers(ImmutableList.of())
//        .description("description")
//        .expectedCount(2)
//        .build();
//
//    given(taskListService.watch()).willReturn(Flux.just(ImmutableList.of(task0, task1)));
//    given(analysisClient.setStatus(eq("task0"), any(ResultStatus.class))).willReturn(Mono.error(new IOException("fail!")));
//    given(analysisClient.setStatus(eq("task1"), any(ResultStatus.class))).willReturn(Mono.just(StorageNodeTest.STORAGE_NODE));
//
//    updater.start();
//
//    verify(analysisClient).setStatus("task0", ResultStatus.STARTING);
//    verify(analysisClient).setStatus("task1", ResultStatus.RUNNING);
//  }
}
