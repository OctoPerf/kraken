package com.kraken.storage.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.storage.entity.StorageNode;
import com.kraken.tools.configuration.jackson.MediaTypes;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import java.time.Duration;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Mono.error;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class StorageWebClient implements StorageClient {

  public static final int NUM_RETRIES = 5;
  public static final Duration FIRST_BACKOFF = Duration.ofMillis(500);

  WebClient webClient;
  ObjectMapper mapper;
  ObjectMapper yamlMapper;

  StorageWebClient(@Qualifier("webClientStorage") final WebClient webClient, final ObjectMapper mapper, @Qualifier("yamlObjectMapper") final ObjectMapper yamlMapper) {
    this.webClient = requireNonNull(webClient);
    this.mapper = requireNonNull(mapper);
    this.yamlMapper = requireNonNull(yamlMapper);
  }

  @Override
  public Mono<StorageNode> createFolder(final String path) {
    return webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/directory").queryParam("path", path).build())
        .retrieve()
        .bodyToMono(StorageNode.class)
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to create folder " + path, t))
        .doOnSubscribe(subscription -> log.info("Creating folder " + path));
  }

  @Override
  public Mono<Boolean> delete(final String path) {
    return webClient.post()
        .uri("/files/delete")
        .body(BodyInserters.fromValue(Collections.singletonList(path)))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Boolean>>() {
        })
        .map(list -> list.get(0))
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to delete file " + path, t))
        .doOnSubscribe(subscription -> log.info("Deleting file " + path));
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
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(clazz)
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to get json file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Getting json file content " + path));
  }

  @Override
  public <T> Mono<T> getYamlContent(final String path, final Class<T> clazz) {
    return this.getContent(path)
        .flatMap(s -> Mono.fromCallable(() -> yamlMapper.readValue(s, clazz)));
  }

  @Override
  public Mono<StorageNode> setContent(final String path, final String content) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/files/set/content")
            .queryParam("path", path)
            .build())
        .body(BodyInserters.fromValue(content))
        .retrieve()
        .bodyToMono(StorageNode.class)
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to set file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Setting file content " + path));
  }

  @Override
  public Mono<String> getContent(final String path) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/files/get/content")
            .queryParam("path", path).build())
        .retrieve()
        .bodyToMono(String.class)
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to get file content " + path, t))
        .doOnSubscribe(subscription -> log.info("Getting file content " + path));
  }

  @Override
  public Mono<Void> downloadFile(final Path localFilePath, final String remotePath) {
    final Flux<DataBuffer> flux = this.getFile(Optional.of(remotePath));
    try {
      return DataBufferUtils.write(flux, new FileOutputStream(localFilePath.toFile(), false).getChannel())
          .map(DataBufferUtils::release)
          .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
          .doOnError(t -> log.error("Failed to download file " + remotePath, t))
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
          .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
          .doOnError(t -> log.error("Failed to download folder " + path, t))
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
        .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(t -> log.error("Failed to upload file " + localFilePath, t))
        .doOnSubscribe(subscription -> log.info(String.format("Uploading local: %s - remote: %s", localFilePath, remotePath)));
  }

  private Mono<StorageNode> setZip(final Path localZipFile, final Optional<String> path) {
    try {
      return webClient.post()
          .uri(uriBuilder -> uriBuilder.path("/files/set/zip")
              .queryParam("path", path.orElse("")).build())
          .body(BodyInserters.fromMultipartData("file", new UrlResource("file", localZipFile.toString())))
          .retrieve()
          .bodyToMono(StorageNode.class)
          .retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
          .doOnError(t -> log.error("Failed to upload zip " + localZipFile, t))
          .doOnSubscribe(subscription -> log.info(String.format("Uploading local: %s - remote: %s", localZipFile, path)));
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
