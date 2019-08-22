package com.kraken.commons.storage.file;

import com.kraken.commons.storage.entity.StorageNode;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

public interface StorageService {

  Flux<StorageNode> list();

  Mono<StorageNode> get(String path);

  Flux<StorageNode> find(String rootPath, Integer maxDepth, String matcher);

  Flux<Boolean> delete(List<String> paths);

  Mono<StorageNode> setDirectory(String path);

  Mono<StorageNode> setFile(String path, Mono<FilePart> file);

  Mono<InputStream> getFile(String path);

  String getFileName(String path);

  Mono<StorageNode> setContent(String path, String content);

  Mono<String> getContent(String path);

  Flux<String> getContent(List<String> paths);

  Mono<StorageNode> rename(String directoryPath, String oldName, String newName);

  Flux<StorageNode> move(List<String> paths, String destination);

  Flux<StorageNode> copy(List<String> paths, String destination);

  Flux<StorageNode> filterExisting(List<StorageNode> nodes);

}
