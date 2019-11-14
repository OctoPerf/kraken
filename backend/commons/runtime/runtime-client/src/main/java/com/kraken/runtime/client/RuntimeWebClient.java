package com.kraken.runtime.client;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.Task;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class RuntimeWebClient implements RuntimeClient {

  WebClient webClient;

  RuntimeWebClient(@Qualifier("webClientRuntime") final WebClient webClient) {
    this.webClient = Objects.requireNonNull(webClient);
  }

  @Override
  public Mono<Task> waitForPredicate(final Predicate<Task> predicate) {
    final Flux<List<Task>> flux = webClient
        .get()
        .uri("/task/watch")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<List<Task>>() {
        });

    return flux
        .doOnSubscribe(subscription -> log.info("Wait for predicate"))
        .map((List<Task> tasks) -> {
          log.info(String.format("Tasks found: %s", tasks.toString()));
          return tasks.stream().filter(predicate).findAny();
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .doOnNext(task -> log.info(String.format("Matching task found: %s", task)))
        .next();
  }

  @Override
  public Mono<Task> waitForStatus(final String taskId, final ContainerStatus status) {
    return this.waitForPredicate(task -> taskId.equals(task.getId()) && task.getStatus().ordinal() >= status.ordinal())
        .doOnSubscribe(subscription -> log.info(String.format("Wait for status %s for task %s", status.toString(), taskId)));
  }

  @Override
  public Mono<Void> setStatus(final String taskId, final String hostId, final String containerId, final ContainerStatus status) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/container/status").pathSegment(status.toString())
            .queryParam("taskId", taskId)
            .queryParam("hostId", hostId)
            .queryParam("containerId", containerId).build())
        .retrieve()
        .bodyToMono(Void.class)
        .doOnSubscribe(subscription -> log.info(String.format("Set status %s for container %s", status.toString(), containerId)));
  }
}
