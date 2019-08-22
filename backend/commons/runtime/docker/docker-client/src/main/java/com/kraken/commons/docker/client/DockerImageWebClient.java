package com.kraken.commons.docker.client;

import com.kraken.commons.docker.entity.DockerImage;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class DockerImageWebClient implements DockerImageClient {

  WebClient webClient;

  DockerImageWebClient(@Qualifier("webClientDockerImage") final WebClient webClient) {
    this.webClient = requireNonNull(webClient);
  }

  @Override
  public Flux<DockerImage> images() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/image").build())
        .retrieve()
        .bodyToFlux(DockerImage.class);
  }

  @Override
  public Mono<Boolean> rmi(final String imageId) {
    return webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/image").queryParam("imageId", imageId).build())
        .retrieve()
        .bodyToMono(String.class)
        .map(Boolean::parseBoolean);
  }

}
