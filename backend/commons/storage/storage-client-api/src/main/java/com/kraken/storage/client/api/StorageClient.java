package com.kraken.storage.client.api;

import com.kraken.security.authentication.client.api.AuthenticatedClient;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.Optional;

public interface StorageClient extends AuthenticatedClient {

  Mono<StorageNode> createFolder(String path);

  Mono<Boolean> delete(String path);

  <T> Mono<StorageNode> setJsonContent(String path, T object);

  <T> Mono<T> getJsonContent(String path, Class<T> clazz);

  <T> Mono<T> getYamlContent(String path, Class<T> clazz);

  Mono<StorageNode> setContent(String path, String content);

  Mono<String> getContent(String path);

  Mono<Void> downloadFile(Path localFolderPath, String path);

  Mono<Void> downloadFolder(Path localFolderPath, String path);

  Mono<StorageNode> uploadFile(Path localFilePath, String remotePath);

  Flux<StorageWatcherEvent> watch();

}
