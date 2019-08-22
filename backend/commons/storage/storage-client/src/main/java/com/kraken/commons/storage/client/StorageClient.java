package com.kraken.commons.storage.client;

import com.kraken.commons.storage.entity.StorageNode;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface StorageClient {
  Mono<StorageNode> createFolder(String path);

  Mono<Boolean> delete(String path);

  <T> Mono<StorageNode> setJsonContent(String path, T object);

  <T> Mono<T> getJsonContent(String path, Class<T> clazz);

  Mono<StorageNode> setContent(String path, String content);

  Mono<String> getContent(String path);

  Flux<DataBuffer> getFile(Optional<String> path);

  Mono<StorageNode> setFile(String path);

}
