package com.kraken.runtime.server.service;

import com.kraken.runtime.backend.api.TaskService;
import com.kraken.runtime.entity.task.FlatContainerTest;
import com.kraken.runtime.entity.task.TaskTest;
import com.kraken.security.entity.owner.PublicOwner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SpringTaskListServiceTest {

  @Mock
  FlatContainersToTask toTask;
  @Mock
  TaskService taskService;

  TaskListService taskListService;

  @Before
  public void before() {
    this.taskListService = new SpringTaskListService(toTask, taskService);
  }

  @Test
  public void shouldList() {
    given(taskService.list(PublicOwner.INSTANCE)).willReturn(Flux.just(FlatContainerTest.CONTAINER));
    given(toTask.apply(any())).willReturn(Mono.just(TaskTest.TASK));

    assertThat(taskListService.list(PublicOwner.INSTANCE).blockFirst()).isEqualTo(TaskTest.TASK);
  }

  @Test
  public void shouldWatch() {
    given(taskService.list(PublicOwner.INSTANCE)).willReturn(Flux.just(FlatContainerTest.CONTAINER));
    given(toTask.apply(any())).willReturn(Mono.just(TaskTest.TASK));

    final var tasks = taskListService.watch(PublicOwner.INSTANCE).take(SpringTaskListService.WATCH_TASKS_DELAY.multipliedBy(3)).collectList().block();
    assertThat(tasks).isNotNull();
    assertThat(tasks.size()).isEqualTo(1);
  }

}

