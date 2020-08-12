package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import com.octoperf.kraken.storage.entity.StorageWatcherEventType;
import com.octoperf.kraken.tools.event.bus.EventBus;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.octoperf.kraken.storage.entity.StorageWatcherEventType.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static org.zeroturnaround.zip.ZipUtil.iterate;
import static org.zeroturnaround.zip.ZipUtil.unpack;
import static reactor.core.publisher.Flux.error;
import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Mono.fromCallable;

@Slf4j
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class FileSystemStorageService implements StorageService {

  @NonNull Owner owner;
  @NonNull Path root;
  @NonNull PathToStorageNode toStorageNode;
  @NonNull EventBus eventBus;

  public void init(final Path applicationPath) {
    synchronized (applicationPath.toString().intern()) {
      final var rootFile = root.toFile();
      if (!rootFile.exists()) {
        if (!rootFile.mkdirs()) {
          throw new RuntimeException("Failed to create directory " + root);
        }
        try (final var stream = Files.walk(applicationPath)) {
          stream.forEach(subFile -> {
            try {
              Files.copy(subFile, root.resolve(applicationPath.relativize(subFile)), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
              throw new RuntimeException(e.getMessage(), e);
            }
          });
        } catch (IOException e) {
          throw new RuntimeException(e.getMessage(), e);
        }
      }
    }
  }

  @Override
  public Flux<StorageWatcherEvent> watch(final String path) {
    return eventBus.of(StorageWatcherEvent.class)
        .filter(event -> event.getOwner().equals(this.owner));
  }

  @Override
  public Flux<StorageNode> list() {
    try {
      return fromStream(Files.walk(root).map(toStorageNode)).filter(StorageNode::notRoot);
    } catch (Exception e) {
      return error(e);
    }
  }

  @Override
  public Mono<StorageNode> get(final String path) {
    return fromCallable(() -> this.toStorageNode.apply(this.stringToPath(path)));
  }

  @Override
  public Flux<StorageWatcherEvent> delete(final List<String> pathsStr) {
    final var paths = this.stringsToPaths(pathsStr);
    return this.callOperation(sink -> {
      paths.forEach(path -> {
        try {
          Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(@NonNull final Path file, @NonNull final BasicFileAttributes attrs) throws IOException {
              final var node = toStorageNode.apply(file);
              Files.delete(file);
              final var event = StorageWatcherEvent.builder()
                  .node(node)
                  .type(StorageWatcherEventType.DELETE)
                  .owner(owner)
                  .build();
              sink.next(event);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(@NonNull final Path dir, final IOException exc) throws IOException {
              final var node = toStorageNode.apply(dir);
              Files.delete(dir);
              final var event = StorageWatcherEvent.builder()
                  .node(node)
                  .type(StorageWatcherEventType.DELETE)
                  .owner(owner)
                  .build();
              sink.next(event);
              return super.postVisitDirectory(dir, exc);
            }
          });
        } catch (IOException e) {
          sink.error(e);
        }
      });
    });
  }

  @Override
  public Flux<StorageWatcherEvent> setDirectory(final String path) {
    return this.callOperation(sink -> {
      log.info(String.format("Set directory %s", path));
      checkArgument(!path.contains(".."), "Cannot store file with relative path outside current directory "
          + path);
      final var completePath = Paths.get(path);
      final var iterator = completePath.iterator();
      var resolved = root;
      // Loop over each part of the path to create required folders and send events
      while (iterator.hasNext()) {
        final var current = iterator.next();
        resolved = resolved.resolve(current);
        log.debug(String.format("Parsing sub-folder %s", resolved.toString()));
        final var file = resolved.toFile();
        if (!file.exists()) {
          if (!file.mkdir()) {
            throw new IOException("Failed to create directory " + resolved);
          }
          sink.next(StorageWatcherEvent.builder()
              .node(toStorageNode.apply(resolved))
              .type(CREATE)
              .owner(this.owner)
              .build());
        }
      }
    });
  }

  @Override
  public Flux<StorageWatcherEvent> setFile(final String path, final Mono<FilePart> file) {
    return file.flatMapMany(part -> {
      final var setFileOperation = this.callOperation(sink -> {
        final var filename = part.filename();
        checkArgument(!filename.contains(".."), "Cannot store file with relative path outside current directory "
            + filename);
        final var currentPath = this.stringToPath(path);
        final var filePath = currentPath.resolve(filename);
        part.transferTo(filePath).block();
        final var created = toStorageNode.apply(filePath);
        sink.next(StorageWatcherEvent.builder()
            .node(created)
            .type(CREATE)
            .owner(this.owner)
            .build());
      });
      return Flux.concat(this.setDirectory(path), setFileOperation);
    });
  }

  @Override
  public Flux<StorageWatcherEvent> setZip(final String path, final Mono<FilePart> file) {
    return file.flatMapMany(part -> {
      final var setZipOperation = this.callOperation(sink -> {
        final var filename = part.filename();
        checkArgument(!filename.contains(".."), "Cannot store file with relative path outside current directory "
            + filename);
        final var currentPath = this.stringToPath(path);
        final var tmp = Files.createTempDirectory(UUID.randomUUID().toString());
        final var zipPath = tmp.resolve(filename);
        part.transferTo(zipPath).block();
        unpack(zipPath.toFile(), currentPath.toFile());
        iterate(zipPath.toFile(), (in, zipEntry) -> sink.next(StorageWatcherEvent.builder()
            .node(toStorageNode.apply(currentPath.resolve(zipEntry.getName())))
            .type(CREATE)
            .owner(this.owner)
            .build()));
        FileSystemUtils.deleteRecursively(tmp);
      });
      return Flux.concat(this.setDirectory(path), setZipOperation);
    });
  }

  @Override
  public Flux<StorageWatcherEvent> extractZip(String path) {
    final var zipPath = this.stringToPath(path);
    return this.callOperation(sink -> {
      final var parent = zipPath.getParent();
      unpack(zipPath.toFile(), zipPath.getParent().toFile());
      iterate(zipPath.toFile(), (in, zipEntry) -> sink.next(StorageWatcherEvent.builder()
          .node(toStorageNode.apply(parent.resolve(zipEntry.getName())))
          .type(CREATE)
          .owner(this.owner)
          .build()));
    });
  }

  @Override
  public Mono<InputStream> getFileInputStream(final String path) {
    return Mono.fromCallable(() -> {
      final var currentPath = this.stringToPath(path);
      return Files.newInputStream(currentPath.toFile().isDirectory() ? this.downloadFolderZip(currentPath) : currentPath);
    });
  }

  @Override
  public Mono<Resource> getFileResource(final String path) {
    return Mono.fromCallable(() -> {
      final var currentPath = this.stringToPath(path);
      return new FileSystemResource(currentPath.toFile().isDirectory() ? this.downloadFolderZip(currentPath) : currentPath);
    });
  }

  @Override
  public String getFileName(final String path) {
    final var file = this.stringToPath(path).toFile();
    return file.isDirectory() ? file.getName() + ".zip" : file.getName();
  }

  @Override
  public Flux<StorageWatcherEvent> setContent(final String path, final String content) {
    log.info(String.format("Set content at path %s: %s", path, content));
    final var completePath = this.stringToPath(path);
    final var parentPath = Optional.ofNullable(Paths.get(path).getParent()).orElse(Path.of("")).toString();
    final var setDirectory = this.stringToPath(parentPath).toFile().exists() ? Flux.<StorageWatcherEvent>empty() : this.setDirectory(parentPath);
    final var setContent = this.callOperation(sink -> {
      final var exists = completePath.toFile().exists();
      Files.writeString(completePath, content);
      final var modified = toStorageNode.apply(completePath);
      sink.next(StorageWatcherEvent.builder()
          .node(modified)
          .type(exists ? MODIFY : CREATE)
          .owner(this.owner)
          .build());
    });
    return Flux.concat(setDirectory, setContent);
  }

  @Override
  public Flux<StorageWatcherEvent> rename(final String directoryPath, final String oldName, final String newName) {
    return this.callOperation(sink -> {
      final var currentPath = this.stringToPath(directoryPath);
      final var oldPath = currentPath.resolve(oldName);
      sink.next(StorageWatcherEvent.builder()
          .node(this.toStorageNode.apply(oldPath))
          .type(DELETE)
          .owner(this.owner)
          .build());
      final var renamed = this.toStorageNode.apply(Files.move(oldPath, currentPath.resolve(newName)));
      sink.next(StorageWatcherEvent.builder()
          .node(renamed)
          .type(CREATE)
          .owner(this.owner)
          .build());
    });
  }

  @Override
  public Mono<String> getContent(String path) {
    return fromCallable(() -> Files.readString(this.stringToPath(path), UTF_8));
  }

  @Override
  public Flux<String> getContent(List<String> paths) {
    return Flux.fromStream(paths.stream()).flatMap(this::getContent);
  }

  @Override
  public Flux<StorageNode> find(final String rootPath, final Integer maxDepth, final String matcher) {
    final var completePath = this.stringToPath(rootPath);
    try {
      final var stream = Files.find(completePath,
          maxDepth,
          (path, basicFileAttributes) -> path.toFile().getName().matches(matcher)
      );
      return Flux.fromStream(stream.filter(path -> !path.equals(completePath))).map(this.toStorageNode);
    } catch (IOException e) {
      return Flux.error(e);
    }
  }

  @Override
  public Flux<StorageNode> filterExisting(List<StorageNode> nodes) {
    return Flux.fromStream(nodes.stream().filter(storageNode -> {
      final var path = this.stringToPath(storageNode.getPath());
      return path.toFile().exists();
    }));
  }

  @Override
  public Flux<StorageWatcherEvent> move(List<String> paths, String destination) {
    return Flux.concat(this.copy(paths, destination), this.delete(paths));
  }

  @Override
  public Flux<StorageWatcherEvent> copy(final List<String> pathsStr, final String destination) {
    return this.callOperation(sink -> {
      final var paths = this.stringsToPaths(pathsStr);
      final var destFolder = this.stringToPath(destination);
      paths.forEach(path -> {
        final var file = path.toFile();
        final var name = path.toFile().getName();
        final var destPath = destFolder.resolve(name);
        try {
          if (file.isFile()) {
            // Copy single file
            if (path.getParent().equals(destFolder)) {
              // Same parent folder => copy with _copy appended at the end
              String[] tokens = name.split("\\.(?=[^\\.]+$)");
              final var copyPath = Paths.get(destFolder.toString(), tokens[0] + "_copy." + tokens[1]);
              // DO not overwrite existing file in this case
              if (!Files.exists(copyPath)) {
                Files.copy(path, copyPath);
                sink.next(StorageWatcherEvent.builder()
                    .node(toStorageNode.apply(copyPath))
                    .type(CREATE)
                    .owner(this.owner)
                    .build());
              }
            } else {
              final var exists = destPath.toFile().exists();
              Files.copy(path, destPath, StandardCopyOption.REPLACE_EXISTING);
              sink.next(StorageWatcherEvent.builder()
                  .node(toStorageNode.apply(destPath))
                  .type(exists ? MODIFY : CREATE)
                  .owner(owner)
                  .build());
            }
          } else {
            // Recursive copy of folders
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
              @Override
              public FileVisitResult preVisitDirectory(@NonNull final Path dir, @NonNull final BasicFileAttributes attrs) throws IOException {
                final var copyPath = destFolder.resolve(name).resolve(path.relativize(dir));
                final var file = copyPath.toFile();
                if (!file.exists()) {
                  if (!file.mkdir()) {
                    throw new IOException("Failed to create directory " + copyPath);
                  }
                  sink.next(StorageWatcherEvent.builder()
                      .node(toStorageNode.apply(copyPath))
                      .type(CREATE)
                      .owner(owner)
                      .build());
                }
                return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult visitFile(@NonNull final Path file, @NonNull final BasicFileAttributes attrs) throws IOException {
                final var copyPath = destFolder.resolve(name).resolve(path.relativize(file));
                final var exists = copyPath.toFile().exists();
                Files.copy(file, copyPath, StandardCopyOption.REPLACE_EXISTING);
                sink.next(StorageWatcherEvent.builder()
                    .node(toStorageNode.apply(copyPath))
                    .type(exists ? MODIFY : CREATE)
                    .owner(owner)
                    .build());
                return FileVisitResult.CONTINUE;
              }
            });
          }
        } catch (IOException e) {
          sink.error(e);
        }
      });
    });
  }

  private Path downloadFolderZip(final Path path) throws IOException {
    final var temp = File.createTempFile("kraken", ".zip");
    final var tempPath = temp.toPath();
    try (final var zos = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(tempPath)))) {
      Files.walk(path)
          .filter(file -> !Files.isDirectory(file))
          .forEach(file -> {
            try {
              zos.putNextEntry(new ZipEntry(path.relativize(file).toString()));
              Files.copy(file, zos);
              zos.closeEntry();
            } catch (IOException e) {
              log.error("Failed to write file into zip " + file, e);
            }
          });
    }
    return tempPath;
  }

  private Flux<StorageWatcherEvent> callOperation(StorageOperation operation) {
    return Flux.<StorageWatcherEvent>create(sink -> {
      try {
        operation.call(sink);
      } catch (Exception e) {
        sink.error(e);
      }
      sink.complete();
    }).doOnNext(eventBus::publish);
  }

  private List<Path> stringsToPaths(final List<String> pathsStr) throws IllegalArgumentException {
    final List<Path> paths = pathsStr.stream().map(this::stringToPath).collect(Collectors.toUnmodifiableList());
    // Remove duplicates
    return paths.stream()
        .filter((current) -> paths.stream().noneMatch((path) -> current.getParent().equals(path)))
        .collect(Collectors.toUnmodifiableList());
  }

  private Path stringToPath(final String path) throws IllegalArgumentException {
    checkArgument(!path.contains(".."), "Cannot store file with relative path outside current directory "
        + path);
    return root.resolve(path);
  }
}
