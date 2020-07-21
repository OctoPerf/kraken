package com.octoperf.kraken.runtime.backend.spring;

import com.octoperf.kraken.runtime.backend.api.FlatContainersToTask;
import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.backend.api.TaskService;
import com.octoperf.kraken.runtime.entity.task.FlatContainerTest;
import com.octoperf.kraken.runtime.entity.task.TaskTest;
import com.octoperf.kraken.security.entity.owner.PublicOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SpringTaskListServiceTest {

  @Mock
  FlatContainersToTask toTask;
  @Mock
  TaskService taskService;

  TaskListService taskListService;

  @BeforeEach
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

