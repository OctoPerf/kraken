package com.kraken.runtime.client;

import com.google.common.collect.ImmutableList;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.Task;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.kraken.runtime.entity.task.ContainerStatus.STARTING;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class RuntimeWebClient implements RuntimeClient {

  public static final int NUM_RETRIES = 5;
  public static final Duration FIRST_BACKOFF = Duration.ofMillis(100);
  WebClient webClient;
  AtomicReference<ContainerStatus> lastStatus;

  RuntimeWebClient(final RuntimeClientProperties properties) {
    this.webClient = WebClient
      .builder()
      .baseUrl(properties.getUrl())
      .build();
    this.lastStatus = new AtomicReference<>(STARTING);
  }

  @Override
  public Mono<Task> waitForPredicate(final FlatContainer container, final Predicate<Task> predicate) {
    final Flux<List<Task>> flux = this.watchTasks(container.getApplicationId());
    return flux
        .map((List<Task> tasks) -> {
          log.info(String.format("Tasks found: %s", tasks.toString()));
          return tasks.stream().filter(predicate).findAny();
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .doOnNext(task -> log.info(String.format("Matching task found: %s", task)))
        .onErrorResume(throwable -> this.setFailedStatus(container).map(aVoid -> Task.builder()
            .id(container.getTaskId())
            .startDate(container.getStartDate())
            .status(ContainerStatus.FAILED)
            .type(container.getTaskType())
            .containers(ImmutableList.of())
            .expectedCount(1)
            .description(container.getDescription())
            .applicationId(container.getApplicationId())
            .build()))
        .next();
  }

  @Override
  public Mono<Task> waitForStatus(final FlatContainer container, final ContainerStatus status) {
    return this.waitForPredicate(container, task -> container.getTaskId().equals(task.getId()) && task.getStatus().ordinal() >= status.ordinal())
        .doOnSubscribe(subscription -> log.info(String.format("Wait for status %s for task %s", status.toString(), container.getTaskId())))
        .doOnError(t -> log.error("Failed to wait for status READY", t));
  }

  @Override
  public Mono<Void> setStatus(final FlatContainer container, final ContainerStatus status) {
    return this._setStatus(container, status)
        .onErrorResume(throwable -> this.setFailedStatus(container));
  }

  @Override
  public Mono<Void> setFailedStatus(FlatContainer container) {
    return this._setStatus(container, ContainerStatus.FAILED)
        .onErrorContinue((throwable, o) -> log.error("Failed to set FAILED status", throwable));
  }

  private Mono<Void> _setStatus(final FlatContainer container, final ContainerStatus status) {
    return Mono.fromCallable(() -> this.lastStatus.get().isTerminal())
        .flatMap(terminal -> terminal ? Mono.empty() : webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("/container/status")
                .pathSegment(status.toString())
                .queryParam("taskId", container.getTaskId())
                .queryParam("containerId", container.getId())
                .queryParam("containerName", container.getName()).build())
            .retrieve()
            .bodyToMono(Void.class)
            .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
            .doOnError(t -> log.error("Failed to set status " + status, t))
            .doOnSuccess(aVoid -> {
              log.info("Set status to " + status);
              this.lastStatus.set(status);
            })
            .doOnSubscribe(subscription -> log.info(String.format("Set status %s -> %s for container %s", this.lastStatus.get(), status.toString(), container.getName()))));
  }

  @Override
  public Mono<FlatContainer> find(final String taskId, final String containerName) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/container/find")
            .queryParam("taskId", taskId)
            .queryParam("containerName", containerName)
            .build())
        .retrieve()
        .bodyToMono(FlatContainer.class)
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnSubscribe(subscription -> log.info(String.format("Find container %s", containerName)));
  }

  @Override
  public Flux<Log> watchLogs(String applicationId) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").pathSegment(applicationId).build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(Log.class)
        .doOnSubscribe(subscription -> log.info("Watch logs"))
        .doOnError(t -> log.error("Failed to watch logs", t));
  }

  @Override
  public Flux<List<Task>> watchTasks(String applicationId) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/task/watch").pathSegment(applicationId).build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<List<Task>>() {
        })
        .doOnSubscribe(subscription -> log.info("Watch tasks"))
        .doOnError(t -> log.error("Failed to wWatch tasks", t));
  }

  public ContainerStatus getLastStatus() {
    return this.lastStatus.get();
  }

}
