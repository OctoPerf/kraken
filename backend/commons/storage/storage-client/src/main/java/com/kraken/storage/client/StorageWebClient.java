package com.kraken.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.storage.entity.StorageNode;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
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
import java.util.*;

import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Mono.error;

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

  @Override
  public Mono<StorageNode> createFolder(final String path) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory").queryParam("path", path).build())
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  @Override
  public Mono<Boolean> delete(final String path) {
    return webClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromObject(Collections.singletonList(path)))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Boolean>>() {
        })
        .map(list -> list.get(0));
  }

  @Override
  public <T> Mono<StorageNode> setJsonContent(final String path, final T object) {
    return this.setContent(path, this.objectToContent(object));
  }

  @Override
  public <T> Mono<T> getJsonContent(final String path, final Class<T> clazz) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/json")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(clazz);
  }

  @Override
  public Mono<StorageNode> setContent(final String path, final String content) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromObject(content))
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  @Override
  public Mono<String> getContent(final String path) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/content")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Mono<Void> downloadFile(final Path localFilePath, final String remotePath) {
    final Flux<DataBuffer> flux = this.getFile(Optional.of(remotePath));
    try {
      return DataBufferUtils.write(flux, new FileOutputStream(localFilePath.toFile(), false).getChannel())
          .map(DataBufferUtils::release)
          .then()
          .doOnSubscribe(subscription -> log.info(String.format("Downloading local: %s - remote: %s", localFilePath, remotePath)));
    } catch (IOException e) {
      log.error("Failed to download file", e);
      return error(e);
    }
  }

  @Override
  public Mono<Void> downloadFolder(final Path localParentFolderPath, final Optional<String> path) {
    final var zipName = UUID.randomUUID().toString() + ".zip";
    final var zipPath = localParentFolderPath.resolve(zipName);

    final Flux<DataBuffer> flux = this.getFile(path);
    try {
      return DataBufferUtils.write(flux, new FileOutputStream(zipPath.toFile()).getChannel())
          .map(DataBufferUtils::release)
          .doOnSubscribe(subscription -> log.info(String.format("Downloading local: %s - remote: %s", localParentFolderPath, path)))
          .then(Mono.fromCallable(() -> {
            ZipUtil.unpack(zipPath.toFile(), localParentFolderPath.toFile());
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
  public Mono<StorageNode> uploadFile(final Path localFilePath, final Optional<String> remotePath) {
    return this.zipLocalFile(localFilePath)
        .flatMap(path -> this.setZip(path, remotePath))
        .doOnSubscribe(subscription -> log.info(String.format("Uploading local: %s - remote: %s", localFilePath, remotePath)));
  }

  private Mono<StorageNode> setZip(final Path localZipFile, final Optional<String> path) {
    try {
      return webClient.post()
          .uri(uriBuilder -> uriBuilder.path("/files/set/zip")
              .queryParam("path", path.orElse("")).build())
          .body(BodyInserters.fromMultipartData("file", new UrlResource("file", localZipFile.toString())))
          .retrieve()
          .bodyToMono(StorageNode.class);
    } catch (MalformedURLException e) {
      log.error("Failed to upload file", e);
      return error(e);
    }
  }

  private Flux<DataBuffer> getFile(final Optional<String> path) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/file")
            .queryParam("path", path.orElse("")).build())
        .retrieve()
        .bodyToFlux(DataBuffer.class);
  }

  private Mono<StorageNode> extractZip(final String path) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/extract/zip")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(StorageNode.class);
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

  private <T> String objectToContent(final T object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
