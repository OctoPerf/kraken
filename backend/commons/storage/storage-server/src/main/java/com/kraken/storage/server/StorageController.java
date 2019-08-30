package com.kraken.storage.server;

import com.kraken.tools.sse.SSEService;
import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageWatcherEvent;
import com.kraken.storage.file.StorageService;
import com.kraken.storage.file.StorageWatcherService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
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
    return this.service.list();
  }

  @GetMapping("/get")
  public Mono<StorageNode> get(@RequestParam(value = "path") final String path) {
    return this.service.get(path);
  }

  @PostMapping("/delete")
  public Flux<Boolean> delete(@RequestBody() final List<String> paths) {
    return service.delete(paths);
  }

  @PostMapping("/rename")
  public Mono<StorageNode> rename(@RequestParam(value = "directoryPath", required = false) final String directoryPath,
                                  @RequestParam("oldName") final String oldName,
                                  @RequestParam("newName") final String newName) {
    return service.rename(nullToEmpty(directoryPath), oldName, newName);
  }

  @PostMapping("/set/directory")
  public Mono<StorageNode> setDirectory(
      @RequestParam("path") final String path) {
    return service.setDirectory(path);
  }

  @GetMapping("/extract/zip")
  public Mono<StorageNode> extractZip(
      @RequestParam("path") final String path) {
    return service.extractZip(path);
  }

  @PostMapping(value = "/set/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<StorageNode> setFile(
      @RequestParam(value = "path", required = false) final String path,
      @RequestPart("file") final Mono<FilePart> file) {
    return service.setFile(nullToEmpty(path), file);
  }

  @GetMapping("/get/file")
  public Mono<ResponseEntity<InputStreamResource>> getFile(
      @RequestParam(value = "path", required = false) final String path) {
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
    final var optionalPath = nullToEmpty(rootPath);
    final var optionalMaxDepth = Optional.ofNullable(maxDepth).orElse(Integer.MAX_VALUE);
    final var optionalMatcher = Optional.ofNullable(matcher).orElse(".*");
    return service.find(optionalPath, optionalMaxDepth, optionalMatcher);
  }

  @PostMapping(value = "/set/content", consumes = TEXT_PLAIN_VALUE)
  public Mono<StorageNode> setContent(
      @RequestParam("path") final String path,
      @RequestBody(required = false) final String content) {
    return service.setContent(path, nullToEmpty(content));
  }

  @GetMapping(value = "/get/content", produces = TEXT_PLAIN_VALUE)
  public Mono<String> getContent(@RequestParam(value = "path") final String path) {
    return this.service.getContent(path);
  }

  @GetMapping(value = "/get/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> getJSON(@RequestParam(value = "path") final String path) {
    return this.service.getContent(path);
  }

  @PostMapping(value = "/list/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> listJSON(@RequestBody() final List<String> paths) {
    final var list = this.service.getContent(paths).collectList().map(strings -> String.join(", ", strings));
    return Flux.concat(Flux.just("["), Flux.from(list), Flux.just("]")).reduce((s, s2) -> s + s2);
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<StorageWatcherEvent>> watch() {
    return this.sse.keepAlive(this.watcher.watch());
  }

  @PostMapping("/copy")
  public Flux<StorageNode> copy(
      @RequestBody() final List<String> paths,
      @RequestParam("destination") final String destination) {
    return service.copy(paths, destination);
  }

  @PostMapping("/move")
  public Flux<StorageNode> move(
      @RequestBody() final List<String> paths,
      @RequestParam("destination") final String destination) {
    return service.move(paths, destination);
  }

  @PostMapping("/filter/existing")
  public Flux<StorageNode> filterExisting(
      @RequestBody() final List<StorageNode> nodes) {
    return service.filterExisting(nodes);
  }
}
