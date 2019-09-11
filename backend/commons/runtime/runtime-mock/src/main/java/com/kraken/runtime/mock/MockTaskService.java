package com.kraken.runtime.mock;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.api.ContainerService;
import com.kraken.runtime.api.TaskService;
import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class MockTaskService implements TaskService, ContainerService {

  AtomicReference<Task> task = new AtomicReference<>(Task.builder()
      .id("taskId")
      .startDate(0L)
      .status(ContainerStatus.CREATING)
      .type(TaskType.DEBUG)
      .containers(ImmutableList.of())
      .description("description")
      .build());

  @Override
  public Mono<String> execute(String applicationId, TaskType taskType, String description, Map<String, String> environment) {
    final var id = UUID.randomUUID().toString();
    task.set(Task.builder()
        .id(id)
        .startDate(0L)
        .status(ContainerStatus.CREATING)
        .type(taskType)
        .containers(ImmutableList.of())
        .description(description)
        .build());
    return Mono.just(id);
  }

  @Override
  public Mono<String> cancel(String applicationId, Task task) {
    this.task.set(null);
    return Mono.just(task.getId());
  }

  @Override
  public Flux<Task> list() {
    return Flux.fromIterable(this.asList());
  }

  @Override
  public Flux<List<Task>> watch() {
    return Flux.interval(Duration.ofSeconds(3))
        .map(aLong -> this.asList())
        .subscribeOn(Schedulers.elastic());
  }

  @Override
  public Mono<Void> attachLogs(String applicationId, Container container) {
    return Mono.fromCallable(() -> null);
  }

  @Override
  public Mono<Void> detachLogs(Container container) {
    return Mono.fromCallable(() -> null);
  }

  @Override
  public Mono<Container> setStatus(String containerId, ContainerStatus status) {
    final var task = this.task.get();

    final var container = Container.builder()
        .id("id")
        .containerId(containerId)
        .taskId(task.getId())
        .taskType(task.getType())
        .name("name")
        .description("description")
        .startDate(0L)
        .status(status)
        .build();

    this.task.set(Task.builder()
        .id(containerId)
        .startDate(0L)
        .status(status)
        .type(TaskType.DEBUG)
        .containers(ImmutableList.of(container))
        .description("description")
        .build());

    return Mono.just(container);
  }

  private List<Task> asList(){
    return Optional.ofNullable(task.get()).map(ImmutableList::of).orElse(ImmutableList.of());
  }
}
