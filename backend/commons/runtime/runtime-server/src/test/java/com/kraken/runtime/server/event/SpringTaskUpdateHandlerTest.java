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
import java.util.Optional;

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

    given(taskListService.watch(Optional.empty())).willReturn(Flux.just(tasksStep0, tasksStep1, tasksStep2, tasksStep3));

    updater.start();

    verify(eventBus).publish(TaskCreatedEvent.builder().task(tasksStep1.get(0)).build());
    verify(eventBus).publish(TaskStatusUpdatedEvent.builder().previousStatus(tasksStep1.get(0).getStatus()).task(tasksStep2.get(0)).build());
    verify(eventBus).publish(TaskRemovedEvent.builder().task(tasksStep2.get(0)).build());
  }
}
