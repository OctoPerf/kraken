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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class MockTaskService implements TaskService, ContainerService {

  AtomicReference<Task> task = new AtomicReference<>();

  @Override
  public Mono<String> execute(final String applicationId,
                              final TaskType taskType,
                              final Integer replicas,
                              final Map<String, String> environment) {
    final var id = "taskId";
    task.set(Task.builder()
        .id(id)
        .startDate(0L)
        .status(ContainerStatus.CREATING)
        .type(taskType)
        .containers(ImmutableList.of())
        .description(environment.get("KRAKEN_DESCRIPTION"))
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
        .distinctUntilChanged()
        .map(tasks -> {
          log.info(String.format("List tasks %s", this.task.get()));
          return tasks;
        })
        .subscribeOn(Schedulers.single());
  }

  @Override
  public Mono<Integer> hostsCount() {
    return Mono.just(1);
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
  public Mono<Container> setStatus(String taskId, String containerId, ContainerStatus status) {
    final var task = this.task.get();

    final var container = Container.builder()
        .id("id")
        .containerId(containerId)
        .hostId("hostId")
        .taskId(task.getId())
        .taskType(task.getType())
        .name("name")
        .description("description")
        .startDate(0L)
        .status(status)
        .build();

    this.task.set(Task.builder()
        .id(task.getId())
        .startDate(0L)
        .status(status)
        .type(TaskType.DEBUG)
        .containers(ImmutableList.of(container))
        .description("description")
        .build());
    log.info(String.format("Set task %s", this.task.get()));
    return Mono.just(container);
  }

  private List<Task> asList() {
    return Optional.ofNullable(task.get()).map(ImmutableList::of).orElse(ImmutableList.of());
  }
}
