package com.kraken.storage.client;

import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.Optional;

public interface StorageClient {

  Mono<StorageNode> createFolder(String path);

  Mono<Boolean> delete(String path);

  <T> Mono<StorageNode> setJsonContent(String path, T object);

  <T> Mono<T> getJsonContent(String path, Class<T> clazz);

  <T> Mono<T> getYamlContent(String path, Class<T> clazz);

  Mono<StorageNode> setContent(String path, String content);

  Mono<String> getContent(String path);

  Mono<Void> downloadFile(Path localFolderPath, String path);

  Mono<Void> downloadFolder(Path localFolderPath, Optional<String> path);

  Mono<StorageNode> uploadFile(Path localFilePath, Optional<String> remotePath);

  Flux<StorageWatcherEvent> watch();

}
