package com.kraken.storage.server;

import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import com.kraken.storage.file.StorageService;
import com.kraken.storage.file.StorageWatcherService;
import com.kraken.tools.sse.SSEService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController()
@RequestMapping("/files")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class StorageController {

  @NonNull
  StorageService service;

  @NonNull
  StorageWatcherService watcher;

  @NonNull
  SSEService sse;

  @GetMapping("/list")
  public Flux<StorageNode> list() {
    log.info("List all nodes");
    return this.service.list();
  }

  @GetMapping("/get")
  public Mono<StorageNode> get(@RequestParam(value = "path") final String path) {
    log.info(String.format("Get file %s", path));
    return this.service.get(path);
  }

  @PostMapping("/delete")
  public Flux<Boolean> delete(@RequestBody() final List<String> paths) {
    log.info(String.format("Delete files %s", String.join(", ", paths)));
    return service.delete(paths);
  }

  @PostMapping("/rename")
  public Mono<StorageNode> rename(@RequestParam(value = "directoryPath", required = false) final String directoryPath,
                                  @RequestParam("oldName") final String oldName,
                                  @RequestParam("newName") final String newName) {
    log.info(String.format("Rename in folder %s from %s to %s", directoryPath, oldName, newName));
    return service.rename(nullToEmpty(directoryPath), oldName, newName);
  }

  @PostMapping("/set/directory")
  public Mono<StorageNode> setDirectory(
      @RequestParam("path") final String path) {
    log.info(String.format("Set directory %s", path));
    return service.setDirectory(path);
  }

  @PostMapping(value = "/set/zip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<StorageNode> setZip(
      @RequestParam(value = "path", required = false) final String path,
      @RequestPart("file") final Mono<FilePart> file) {
    log.info(String.format("Set zip %s", path));
    return service.setZip(nullToEmpty(path), file);
  }

  @PostMapping(value = "/set/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<StorageNode> setFile(
      @RequestParam(value = "path", required = false) final String path,
      @RequestPart("file") final Mono<FilePart> file) {
    log.info(String.format("Set file %s", path));
    return service.setFile(nullToEmpty(path), file);
  }

  @GetMapping("/extract/zip")
  public Mono<StorageNode> extractZip(
      @RequestParam("path") final String path) {
    log.info(String.format("Extract zip at %s", path));
    return service.extractZip(path);
  }

  @GetMapping("/get/file")
  public Mono<ResponseEntity<InputStreamResource>> getFile(
      @RequestParam(value = "path", required = false) final String path) {
    log.info(String.format("Get file %s", path));
    final var optionalPath = nullToEmpty(path);
    return service.getFile(optionalPath).map(is -> ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", service.getFileName(optionalPath)))
        .body(new InputStreamResource(is))
    );
  }

  @GetMapping("/find")
  public Flux<StorageNode> find(@RequestParam(value = "rootPath", required = false) final String rootPath,
                                @RequestParam(value = "maxDepth", required = false) final Integer maxDepth,
                                @RequestParam(value = "matcher", required = false) final String matcher) {
    log.info(String.format("Find rootPath %s, maxDepth %d, matcher %s", rootPath, maxDepth, matcher));
    final var optionalPath = nullToEmpty(rootPath);
    final var optionalMaxDepth = Optional.ofNullable(maxDepth).orElse(Integer.MAX_VALUE);
    final var optionalMatcher = Optional.ofNullable(matcher).orElse(".*");
    return service.find(optionalPath, optionalMaxDepth, optionalMatcher);
  }

  @PostMapping(value = "/set/content", consumes = TEXT_PLAIN_VALUE)
  public Mono<StorageNode> setContent(
      @RequestParam("path") final String path,
      @RequestBody(required = false) final String content) {
    log.debug(String.format("Set content for %s :%n%s", path, content));
    return service.setContent(path, nullToEmpty(content));
  }

  @GetMapping(value = "/get/content", produces = TEXT_PLAIN_VALUE)
  public Mono<String> getContent(@RequestParam(value = "path") final String path) {
    log.debug(String.format("Get content of %s", path));
    return this.service.getContent(path);
  }

  @GetMapping(value = "/get/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> getJSON(@RequestParam(value = "path") final String path) {
    log.info(String.format("Get JSON for %s", path));
    return this.service.getContent(path);
  }

  @PostMapping(value = "/list/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> listJSON(@RequestBody() final List<String> paths) {
    log.info(String.format("List JSON for %s", String.join(", ", paths)));
    final var list = this.service.getContent(paths).collectList().map(strings -> String.join(", ", strings));
    return Flux.concat(Flux.just("["), Flux.from(list), Flux.just("]")).reduce((s, s2) -> s + s2);
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<StorageWatcherEvent>> watch() {
    log.info("Watch storage events");
    return this.sse.keepAlive(this.watcher.watch());
  }

  @PostMapping("/copy")
  public Flux<StorageNode> copy(
      @RequestBody() final List<String> paths,
      @RequestParam("destination") final String destination) {
    log.info(String.format("Copy %s to %s", String.join(", ", paths), destination));
    return service.copy(paths, destination);
  }

  @PostMapping("/move")
  public Flux<StorageNode> move(
      @RequestBody() final List<String> paths,
      @RequestParam("destination") final String destination) {
    log.info(String.format("Move %s to %s", String.join(", ", paths), destination));
    return service.move(paths, destination);
  }

  @PostMapping("/filter/existing")
  public Flux<StorageNode> filterExisting(
      @RequestBody() final List<StorageNode> nodes) {
    log.info(String.format("Filter %d existing nodes", nodes.size()));
    return service.filterExisting(nodes);
  }
}
