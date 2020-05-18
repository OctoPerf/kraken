package com.kraken.runtime.server.service;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.Task;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.runtime.event.TaskCreatedEvent;
import com.kraken.runtime.event.TaskRemovedEvent;
import com.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.kraken.security.entity.owner.ApplicationOwnerTest;
import com.kraken.security.entity.owner.PublicOwner;
import com.kraken.tools.event.bus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
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
        .type(TaskType.GATLING_RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .owner(ApplicationOwnerTest.APPLICATION_OWNER)
        .build());
    final var tasksStep2 = ImmutableList.of(Task.builder()
        .id("taskId")
        .startDate(42L)
        .status(ContainerStatus.STARTING)
        .type(TaskType.GATLING_RUN)
        .containers(ImmutableList.of())
        .description("description")
        .expectedCount(2)
        .owner(ApplicationOwnerTest.APPLICATION_OWNER)
        .build());
    final var tasksStep3 = ImmutableList.<Task>of();

    given(taskListService.watch(PublicOwner.INSTANCE)).willReturn(Flux.just(tasksStep0, tasksStep1, tasksStep2, tasksStep3));

    updater.start();

    verify(eventBus).publish(TaskCreatedEvent.builder().task(tasksStep1.get(0)).build());
    verify(eventBus).publish(TaskStatusUpdatedEvent.builder().previousStatus(tasksStep1.get(0).getStatus()).task(tasksStep2.get(0)).build());
    verify(eventBus).publish(TaskRemovedEvent.builder().task(tasksStep2.get(0)).build());
  }
}
