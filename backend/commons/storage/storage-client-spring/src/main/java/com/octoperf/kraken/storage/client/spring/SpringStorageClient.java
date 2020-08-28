package com.octoperf.kraken.storage.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.file.StorageService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringStorageClient implements StorageClient {

  @NonNull
  StorageService service;
  @NonNull
  ObjectMapper mapper;
  @NonNull
  ObjectMapper yamlMapper;

  @Override
  public Mono<Void> init() {
    return service.init();
  }

  @Override
  public Mono<StorageNode> createFolder(final String path) {
    return eventsToNode(service.setDirectory(path), path);
  }

  @Override
  public Mono<Boolean> delete(String path) {
    return service.delete(ImmutableList.of(path)).collectList().map(events -> true).onErrorReturn(false);
  }

  @Override
  public <T> Mono<StorageNode> setJsonContent(String path, T object) {
    return Mono.fromCallable(() -> mapper.writeValueAsString(object))
        .flatMap(content -> this.setContent(path, content));
  }

  @Override
  public <T> Mono<T> getJsonContent(String path, Class<T> clazz) {
    return service.getContent(path).flatMap(content -> Mono.fromCallable(() -> mapper.readValue(content, clazz)));
  }

  @Override
  public <T> Mono<T> getYamlContent(String path, Class<T> clazz) {
    return service.getContent(path).flatMap(content -> Mono.fromCallable(() -> yamlMapper.readValue(content, clazz)));
  }

  @Override
  public Mono<StorageNode> setContent(String path, String content) {
    return eventsToNode(service.setContent(path, content), path);
  }

  @Override
  public Flux<StorageNode> find(final String rootPath, final Integer maxDepth, final String matcher) {
    return service.find(rootPath, maxDepth, matcher);
  }

  @Override
  public Mono<String> getContent(String path) {
    return service.getContent(path);
  }

  @Override
  public Mono<Void> downloadFile(Path localFolderPath, String path) {
    return Mono.error(new UnsupportedOperationException());
  }

  @Override
  public Mono<Void> downloadFolder(Path localFolderPath, String path) {
    return Mono.error(new UnsupportedOperationException());
  }

  @Override
  public Flux<StorageWatcherEvent> uploadFile(Path localFilePath, String remotePath) {
    return Flux.error(new UnsupportedOperationException());
  }

  @Override
  public Flux<StorageWatcherEvent> watch() {
    return service.watch("");
  }
}
