package com.kraken.commons.storage.client;

import com.kraken.commons.storage.entity.StorageNode;
import reactor.core.publisher.Mono;

public interface StorageClient {
  Mono<StorageNode> createFolder(String path);

  Mono<Boolean> delete(String path);

  <T> Mono<StorageNode> setJsonContent(String path, T object);

  <T> Mono<T> getJsonContent(String path, Class<T> clazz);

  Mono<StorageNode> setContent(String path, String content);

  Mono<String> getContent(String path);
}
