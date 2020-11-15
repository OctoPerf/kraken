package com.octoperf.kraken.storage.server;

import com.google.common.base.Strings;
import com.octoperf.kraken.security.authentication.api.UserProvider;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.file.StorageService;
import com.octoperf.kraken.storage.file.StorageServiceBuilder;
import com.octoperf.kraken.tools.sse.SSEService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Slf4j
@RestController
@RequestMapping("/files")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class StorageController {

  @NonNull StorageServiceBuilder serviceBuilder;
  @NonNull SSEService sse;
  @NonNull UserProvider userProvider;

  private Mono<StorageService> getService(final String applicationId, @Nullable final String projectId) {
    return this.userProvider.getOwner(applicationId, Strings.nullToEmpty(projectId)).map(serviceBuilder::build);
  }

  @GetMapping("/static/**")
  public Mono<ResponseEntity<Resource>> getStatic(final ServerWebExchange payload) {
    final var split = payload.getRequest().getPath().value().split("/", 6);
    final var applicationId = split[3];
    final var projectId = split[4];
    final var filePath = split.length >= 6 ? split[5] : "";
    final var path = URLDecoder.decode(applicationId + File.separator + filePath, Charset.defaultCharset());
    log.info(String.format("Get static file %s", path));
    return this.getService(applicationId, projectId)
        .flatMap(service -> service.getFileResource(path))
        .flatMap(fileSystemResource -> Mono.fromCallable(() -> {
              final var mediaType = Mono.fromCallable(() -> MediaType.valueOf(Files.probeContentType(Path.of(fileSystemResource.getFilename()))))
                  .onErrorReturn(MediaType.APPLICATION_OCTET_STREAM)
                  .blockOptional();
              return ResponseEntity.ok()
                  .contentLength(fileSystemResource.contentLength())
                  .contentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM))
                  .body(fileSystemResource);
            }
        ));
  }

  @GetMapping("/list")
  public Flux<StorageNode> list(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                @RequestHeader(name = "ProjectId", required = false) @Pattern(regexp = "[a-z0-9]{10}") final String projectId) {
    log.info("List all nodes");
    return this.getService(applicationId, projectId)
        .flatMapMany(StorageService::list);
  }

  @GetMapping("/get")
  public Mono<StorageNode> get(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                               @RequestHeader(name = "ProjectId", required = false) final String projectId,
                               @RequestParam(value = "path") final String path) {
    log.info(String.format("Get file %s", path));
    return this.getService(applicationId, projectId)
        .flatMap(service -> service.get(path));
  }

  @PostMapping("/delete")
  public Flux<StorageWatcherEvent> delete(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                          @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                          @RequestBody() final List<String> paths) {
    log.info(String.format("Delete files %s", String.join(", ", paths)));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.delete(paths));
  }

  @PostMapping("/rename")
  public Flux<StorageWatcherEvent> rename(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                          @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                          @RequestParam(value = "directoryPath", required = false) final String directoryPath,
                                          @RequestParam("oldName") final String oldName,
                                          @RequestParam("newName") final String newName) {
    log.info(String.format("Rename in folder %s from %s to %s", directoryPath, oldName, newName));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.rename(nullToEmpty(directoryPath), oldName, newName));
  }

  @PostMapping("/set/directory")
  public Flux<StorageWatcherEvent> setDirectory(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                                @RequestParam("path") final String path) {
    log.info(String.format("Set directory %s", path));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.setDirectory(path));
  }

  @PostMapping(value = "/set/zip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Flux<StorageWatcherEvent> setZip(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                          @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                          @RequestParam(value = "path", required = false) final String path,
                                          @RequestPart("file") final Mono<FilePart> file) {
    log.info(String.format("Set zip %s", path));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.setZip(nullToEmpty(path), file));
  }

  @PostMapping(value = "/set/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Flux<StorageWatcherEvent> setFile(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                           @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                           @RequestParam(value = "path", required = false) final String path,
                                           @RequestPart("file") final Mono<FilePart> file) {
    log.info(String.format("Set file %s", path));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.setFile(nullToEmpty(path), file));
  }

  @GetMapping("/extract/zip")
  public Flux<StorageWatcherEvent> extractZip(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                              @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                              @RequestParam("path") final String path) {
    log.info(String.format("Extract zip at %s", path));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.extractZip(path));
  }

  @GetMapping("/get/file")
  public Mono<ResponseEntity<InputStreamResource>> getFile(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                           @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                                           @RequestParam(value = "path", required = false) final String path) {
    log.info(String.format("Get file %s", path));
    final var optionalPath = nullToEmpty(path);
    return this.getService(applicationId, projectId).flatMap(service -> service.getFileInputStream(optionalPath).map(is -> ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", service.getFileName(optionalPath)))
        .body(new InputStreamResource(is)))
    );
  }

  @GetMapping("/find")
  public Flux<StorageNode> find(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                @RequestParam(value = "rootPath", required = false) final String rootPath,
                                @RequestParam(value = "maxDepth", required = false) final Integer maxDepth,
                                @RequestParam(value = "matcher", required = false) final String matcher) {
    log.info(String.format("Find rootPath %s, maxDepth %d, matcher %s", rootPath, maxDepth, matcher));
    final var optionalPath = nullToEmpty(rootPath);
    final var optionalMaxDepth = Optional.ofNullable(maxDepth).orElse(Integer.MAX_VALUE);
    final var optionalMatcher = Optional.ofNullable(matcher).orElse(".*");
    return this.getService(applicationId, projectId).flatMapMany(service -> service.find(optionalPath, optionalMaxDepth, optionalMatcher));
  }

  @PostMapping(value = "/set/content", consumes = TEXT_PLAIN_VALUE)
  public Flux<StorageWatcherEvent> setContent(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                              @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                              @RequestParam("path") final String path,
                                              @RequestBody(required = false) final String content) {
    log.debug(String.format("Set content for %s :%n%s", path, content));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.setContent(path, nullToEmpty(content)));
  }

  @GetMapping(value = "/get/content", produces = TEXT_PLAIN_VALUE)
  public Mono<String> getContent(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                 @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                 @RequestParam(value = "path") final String path) {
    log.debug(String.format("Get content of %s", path));
    return this.getService(applicationId, projectId).flatMap(service -> service.getContent(path));
  }

  @GetMapping(value = "/get/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> getJSON(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                              @RequestHeader(name = "ProjectId", required = false) final String projectId,
                              @RequestParam(value = "path") final String path) {
    log.info(String.format("Get JSON for %s", path));
    return this.getService(applicationId, projectId).flatMap(service -> service.getContent(path));
  }

  @PostMapping(value = "/list/json", produces = APPLICATION_JSON_VALUE)
  public Mono<String> listJSON(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                               @RequestHeader(name = "ProjectId", required = false) final String projectId,
                               @RequestBody() final List<String> paths) {
    log.info(String.format("List JSON for [%s]", String.join(", ", paths)));
    final var list = this.getService(applicationId, projectId).flatMapMany(service -> service.getContent(paths)).collectList().map(strings -> String.join(", ", strings));
    return Flux.concat(Flux.just("["), Flux.from(list), Flux.just("]")).reduce((s, s2) -> s + s2);
  }

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<StorageWatcherEvent>> watch(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                                          @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                                          @RequestParam(value = "root", required = false) final String root) {
    log.info("Watch storage events");
    return this.getService(applicationId, projectId).flatMapMany(service -> this.sse.keepAlive(service.watch(nullToEmpty(root))).map(event -> {
      log.debug(event.toString());
      return event;
    }));
  }

  @PostMapping("/copy")
  public Flux<StorageWatcherEvent> copy(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                        @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                        @RequestBody() final List<String> paths,
                                        @RequestParam("destination") final String destination) {
    log.info(String.format("Copy %s to %s", String.join(", ", paths), destination));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.copy(paths, destination));
  }

  @PostMapping("/move")
  public Flux<StorageWatcherEvent> move(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                        @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                        @RequestBody() final List<String> paths,
                                        @RequestParam("destination") final String destination) {
    log.info(String.format("Move %s to %s", String.join(", ", paths), destination));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.move(paths, destination));
  }

  @PostMapping("/filter/existing")
  public Flux<StorageNode> filterExisting(@RequestHeader("ApplicationId") @Pattern(regexp = "[a-z0-9]*") final String applicationId,
                                          @RequestHeader(name = "ProjectId", required = false) final String projectId,
                                          @RequestBody() final List<StorageNode> nodes) {
    log.info(String.format("Filter %d existing nodes", nodes.size()));
    return this.getService(applicationId, projectId).flatMapMany(service -> service.filterExisting(nodes));
  }
}
