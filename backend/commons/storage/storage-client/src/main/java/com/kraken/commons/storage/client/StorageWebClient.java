package com.kraken.commons.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.commons.storage.entity.StorageNode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class StorageWebClient implements StorageClient {

  WebClient webClient;
  ObjectMapper mapper;

  StorageWebClient(@Qualifier("webClientStorage") final WebClient webClient, final ObjectMapper mapper) {
    this.webClient = Objects.requireNonNull(webClient);
    this.mapper = Objects.requireNonNull(mapper);
  }

  public Mono<StorageNode> createFolder(final String path) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory").queryParam("path", path).build())
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  public Mono<Boolean> delete(final String path) {
    return webClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromObject(Collections.singletonList(path)))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Boolean>>() { })
        .map(list -> list.get(0));
  }

  public <T> Mono<StorageNode> setJsonContent(final String path, final T object) {
    return this.setContent(path, this.objectToContent(object));
  }

  public <T> Mono<T> getJsonContent(final String path, final Class<T> clazz) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/json")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(clazz);
  }

  public Mono<StorageNode> setContent(final String path, final String content) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromObject(content))
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  public Mono<String> getContent(final String path) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/content")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(String.class);
  }

  private <T> String objectToContent(final T object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
