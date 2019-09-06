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
  public Mono<Task> waitForStatus(final String taskId, final ContainerStatus status) {
    final Flux<List<Task>> flux = webClient
        .get()
        .uri("/task/watch")
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<List<Task>>() {
        });

    return flux
        .map(tasks -> tasks.stream().filter(task -> taskId.equals(task.getId()) && status.equals(task.getStatus())).findAny())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .next();
  }

  @Override
  public Mono<Container> setStatus(final String containerId, final ContainerStatus status) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/container/status").pathSegment(status.toString()).queryParam("containerId", containerId).build())
        .retrieve()
        .bodyToMono(Container.class);
  }
}
