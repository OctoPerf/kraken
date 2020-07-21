package com.octoperf.kraken.runtime.backend.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.runtime.event.TaskCreatedEvent;
import com.octoperf.kraken.runtime.event.TaskRemovedEvent;
import com.octoperf.kraken.runtime.event.TaskStatusUpdatedEvent;
import com.octoperf.kraken.security.entity.owner.ApplicationOwnerTest;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import com.octoperf.kraken.tools.event.bus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpringTaskUpdateHandlerTest {

  @Mock
  TaskListService taskListService;

  @Mock
  EventBus eventBus;

  SpringTaskUpdateHandler updater;

  @BeforeEach
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
