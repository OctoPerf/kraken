package com.kraken.commons.docker.client;

import com.kraken.commons.docker.entity.DockerContainer;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class DockerContainerWebClient implements DockerContainerClient {

  WebClient webClient;

  DockerContainerWebClient(@Qualifier("webClientDockerContainer") final WebClient webClient) {
    this.webClient = requireNonNull(webClient);
  }

  @Override
  public Mono<String> run(final String name, final String config) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/run").queryParam("name", name).build())
        .contentType(MediaType.TEXT_PLAIN)
        .body(BodyInserters.fromObject(config))
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Mono<Boolean> start(final String containerId) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/container/start").queryParam("containerId", containerId).build())
        .retrieve()
        .bodyToMono(String.class)
        .map(Boolean::parseBoolean);
  }

  @Override
  public Mono<Boolean> stop(final String containerId, final Optional<Integer> secondsToWaitBeforeKilling) {
    return webClient.delete()
        .uri(uriBuilder -> {
          uriBuilder.path("/container/stop")
              .queryParam("containerId", containerId);
          secondsToWaitBeforeKilling.ifPresent(value -> uriBuilder.queryParam("secondsToWaitBeforeKilling", value));
          return uriBuilder.build();
        })
        .retrieve()
        .bodyToMono(String.class)
        .map(Boolean::parseBoolean);
  }

  @Override
  public Mono<String> tail(final String containerId, final Optional<Integer> lines) {
    return webClient.get()
        .uri(uriBuilder -> {
          uriBuilder.path("/container/tail")
              .queryParam("containerId", containerId);
          lines.ifPresent(value -> uriBuilder.queryParam("lines", value));
          return uriBuilder.build();
        })
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Flux<DockerContainer> ps() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container").build())
        .retrieve()
        .bodyToFlux(DockerContainer.class);
  }

  @Override
  public Mono<DockerContainer> inspect(final String containerId) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/container").queryParam("containerId", containerId).build())
        .retrieve()
        .bodyToMono(DockerContainer.class);
  }

  @Override
  public Mono<Boolean> rm(final String containerId) {
    return webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/container").queryParam("containerId", containerId).build())
        .retrieve()
        .bodyToMono(String.class)
        .map(Boolean::parseBoolean);
  }

}
