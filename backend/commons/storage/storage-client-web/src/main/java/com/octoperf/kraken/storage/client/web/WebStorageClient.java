package com.octoperf.kraken.storage.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.zeroturnaround.zip.ZipUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Mono.error;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebStorageClient implements StorageClient {

  @NonNull
  WebClient webClient;
  @NonNull
  ObjectMapper mapper;
  @NonNull
  ObjectMapper yamlMapper;

  @Override
  public Flux<StorageWatcherEvent> watch() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/watch").build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(StorageWatcherEvent.class)
        .doOnError(t -> log.error("Failed to watch storage", t))
        .doOnSubscribe(subscription -> log.info("Watching storage"));
  }

  @Override
  public Mono<StorageNode> createFolder(final String path) {
    return retry(eventsToNode(webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory").queryParam("path", path).build())
        .retrieve()
        .bodyToFlux(StorageWatcherEvent.class), path)
        .doOnError(t -> log.error("Failed to create folder " + path, t))
        .doOnSubscribe(subscription -> log.info("Creating folder " + path)), log);
  }

  @Override
  public Mono<Boolean> delete(final String path) {
    return retry(webClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromValue(Collections.singletonList(path)))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StorageWatcherEvent>>() {
        })
        .map(list -> !list.isEmpty())
        .doOnError(t -> log.error("Failed to delete file " + path, t))
        .onErrorReturn(false)
        .doOnSubscribe(subscription -> log.info("Deleting file " + path)), log);
  }

  @Override
  public <T> Mono<StorageNode> setJsonContent(final String path, final T object) {
    return Mono.fromCallable(() -> mapper.writeValueAsString(object))
        .flatMap(content -> this.setContent(path, content));
  }

  @Override
  public <T> Mono<T> getJsonContent(final String path, final Class<T> clazz) {
    return retry(webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/json")
            .queryParam("path", path).build())
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(clazz)
        .doOnError(t -> log.error("Failed to get json file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Getting json file content " + path)), log);
  }

  @Override
  public <T> Mono<T> getYamlContent(final String path, final Class<T> clazz) {
    return this.getContent(path)
        .flatMap(s -> Mono.fromCallable(() -> yamlMapper.readValue(s, clazz)));
  }

  @Override
  public Mono<StorageNode> setContent(final String path, final String content) {
    return retry(eventsToNode(webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromValue(content))
        .retrieve()
        .bodyToFlux(StorageWatcherEvent.class), path)
        .doOnError(t -> log.error("Failed to set file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Setting file content " + path)), log);
  }

  @Override
  public Mono<String> getContent(final String path) {
    return retry(webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/content")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(String.class)
        .doOnError(t -> log.error("Failed to get file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Getting file content " + path)), log);
  }

  @Override
  public Mono<Void> downloadFile(final Path localFilePath, final String remotePath) {
    final Flux<DataBuffer> flux = this.getFile(remotePath);
    try {
      return retry(DataBufferUtils.write(flux, new FileOutputStream(localFilePath.toFile(), false).getChannel())
          .map(DataBufferUtils::release)
          .doOnError(t -> log.error("Failed to download file " + remotePath, t)), log)
          .then()
          .doOnSubscribe(subscription -> log.info(String.format("Downloading local: %s - remote: %s", localFilePath, remotePath)));
    } catch (IOException e) {
      log.error("Failed to download file", e);
      return error(e);
    }
  }

  @Override
  public Mono<Void> downloadFolder(final Path localParentFolderPath, final String path) {
    final var zipName = UUID.randomUUID().toString() + ".zip";
    final var zipPath = localParentFolderPath.resolve(zipName);
    final Flux<DataBuffer> flux = this.getFile(path);
    try {
      return retry(DataBufferUtils.write(flux, new FileOutputStream(zipPath.toFile()).getChannel())
              .map(DataBufferUtils::release)
              .doOnError(t -> log.error("Failed to download folder " + path, t))
              .doOnSubscribe(subscription -> log.info(String.format("Downloading local: %s - remote: %s", localParentFolderPath, path)))
          , log)
          .then(Mono.fromCallable(() -> {
            ZipUtil.unpack(zipPath.toFile(), localParentFolderPath.toFile());
            ZipUtil.iterate(zipPath.toFile(), (inputStream, zipEntry) -> log.info(zipEntry.getName()));
            try {
              Files.delete(zipPath);
            } catch (IOException e) {
              log.error("Failed to delete downloaded Zip file", e);
              throw new RuntimeException(e);
            }
            return null;
          }));
    } catch (FileNotFoundException e) {
      log.error("Failed to download folder", e);
      return error(e);
    }
  }

  @Override
  public Flux<StorageWatcherEvent> uploadFile(final Path localFilePath, final String remotePath) {
    return retry(this.zipLocalFile(localFilePath)
        .flatMapMany(path -> this.setZip(path, remotePath))
        .doOnError(t -> log.error("Failed to upload file " + localFilePath, t))
        .doOnSubscribe(subscription -> log.info(String.format("Uploading local: %s - remote: %s", localFilePath, remotePath))), log);
  }

  private Flux<StorageWatcherEvent> setZip(final Path localZipFile, final String path) {
    try {
      return retry(webClient.post()
          .uri(uri -> uri.path("/files/set/zip").queryParam("path", path).build())
          .body(BodyInserters.fromMultipartData("file", new UrlResource("file", localZipFile.toString())))
          .retrieve()
          .bodyToFlux(StorageWatcherEvent.class)
          .doOnError(t -> log.error("Failed to upload zip " + localZipFile, t))
          .doOnSubscribe(subscription -> log.info(String.format("Uploading local: %s - remote: %s", localZipFile, path))), log);
    } catch (MalformedURLException e) {
      log.error("Failed to upload file", e);
      return Flux.error(e);
    }
  }

  private Flux<DataBuffer> getFile(final String path) {
    return retry(webClient.get()
        .uri(uri -> uri.path("/files/get/file").queryParam("path", path).build())
        .retrieve()
        .bodyToFlux(DataBuffer.class), log);
  }


  private Mono<Path> zipLocalFile(final Path path) {
    return Mono.fromCallable(() -> {
      final var tmp = File.createTempFile(UUID.randomUUID().toString(), ".zip");
      tmp.deleteOnExit();
      final var file = path.toFile();
      if (file.isDirectory()) {
        ZipUtil.pack(file, tmp);
      } else {
        ZipUtil.packEntry(file, tmp);
      }
      return tmp.toPath();
    });
  }
}
