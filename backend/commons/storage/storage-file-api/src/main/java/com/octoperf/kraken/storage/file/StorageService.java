package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageInitMode;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

public interface StorageService {

  Mono<Void> init(StorageInitMode mode);

  Flux<StorageNode> list();

  Mono<StorageNode> get(String path);

  Flux<StorageNode> find(String rootPath, Integer maxDepth, String matcher);

  Flux<StorageWatcherEvent> delete(List<String> paths);

  Flux<StorageWatcherEvent> setDirectory(String path);

  Flux<StorageWatcherEvent> setFile(String path, Mono<FilePart> file);

  Flux<StorageWatcherEvent> setZip(String path, Mono<FilePart> file);

  Mono<InputStream> getFileInputStream(String path);

  Mono<Resource> getFileResource(String path);

  String getFileName(String path);

  Flux<StorageWatcherEvent> setContent(String path, String content);

  Mono<String> getContent(String path);

  Flux<String> getContent(List<String> paths);

  Flux<StorageWatcherEvent> rename(String directoryPath, String oldName, String newName);

  Flux<StorageWatcherEvent> move(List<String> paths, String destination);

  Flux<StorageWatcherEvent> copy(List<String> paths, String destination);

  Flux<StorageNode> filterExisting(List<StorageNode> nodes);

  Flux<StorageWatcherEvent> extractZip(String path);

  Flux<StorageWatcherEvent> watch(String path);

}
