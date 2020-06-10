package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageNode;
import com.octoperf.kraken.storage.entity.StorageWatcherEvent;
import io.methvin.watcher.DirectoryChangeEvent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.FileSystemUtils.deleteRecursively;
import static org.zeroturnaround.zip.ZipUtil.unpack;
import static reactor.core.publisher.Flux.error;
import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Mono.fromCallable;
import static reactor.core.publisher.Mono.just;

@Slf4j
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class FileSystemStorageService implements StorageService {

  @NonNull Path root;
  @NonNull PathToStorageNode toStorageNode;
  @NonNull Flux<DirectoryChangeEvent> watcherEventFlux;

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
    return Flux.from(watcherEventFlux)
        .filter(event -> event.path().startsWith(stringToPath(path).toString()))
        .map(event -> StorageWatcherEvent.builder()
            .node(toStorageNode.apply(event.path()))
            .event(event.eventType().toString())
            .build());
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
  public Flux<Boolean> delete(final List<String> paths) {
    return Flux.fromStream(
        paths.stream()
            .sorted(Comparator.comparing(paths::indexOf).reversed())
            .map(path -> Mono.fromCallable(() -> deleteRecursively(this.stringToPath(path))))
            .map(Mono::block));
  }

  @Override
  public Mono<StorageNode> setDirectory(String path) {
    return fromCallable(() -> {
      final var completePath = this.stringToPath(path);
      final var file = completePath.toFile();
      if (!file.exists() && !file.mkdirs()) {
        throw new IOException("Failed to create directory " + path);
      }
      return this.toStorageNode.apply(completePath);
    });
  }

  @Override
  public Mono<StorageNode> setFile(final String path, final Mono<FilePart> file) {
    return this.setDirectory(path)
        .then(file.map((part) -> {
          final var filename = part.filename();
          checkArgument(!filename.contains(".."), "Cannot store file with relative path outside current directory "
              + filename);
          final var currentPath = this.stringToPath(path);
          final var filePath = currentPath.resolve(filename);
          part.transferTo(filePath).block();
          return toStorageNode.apply(filePath);
        }));
  }

  @Override
  public Mono<StorageNode> setZip(final String path, final Mono<FilePart> file) {
    return this.setDirectory(path)
        .then(file.flatMap((part) -> {
          try {
            final var filename = part.filename();
            checkArgument(!filename.contains(".."), "Cannot store file with relative path outside current directory "
                + filename);
            final var currentPath = this.stringToPath(path);
            final var tmp = Files.createTempDirectory(UUID.randomUUID().toString());
            final var zipPath = tmp.resolve(filename);
            part.transferTo(zipPath).block();
            unpack(zipPath.toFile(), currentPath.toFile());
            deleteRecursively(tmp);
            return Mono.just(toStorageNode.apply(currentPath));
          } catch (IOException e) {
            return Mono.error(e);
          }
        }));
  }

  @Override
  public Mono<InputStream> getFile(final String path) {
    try {
      final var currentPath = this.stringToPath(path);
      return just(currentPath.toFile().isDirectory() ? this.downloadFolderZip(currentPath) : this.downloadFile(currentPath));
    } catch (IOException e) {
      return Mono.error(e);
    }
  }

  @Override
  public String getFileName(final String path) {
    final var file = this.stringToPath(path).toFile();
    return file.isDirectory() ? file.getName() + ".zip" : file.getName();
  }

  @Override
  public Mono<StorageNode> setContent(final String path, final String content) {
    return fromCallable(() -> {
      final var completePath = this.stringToPath(path);
      final var parent = completePath.getParent().toFile();
      if (!parent.exists() && !parent.mkdirs()) {
        throw new IOException("Failed to create directory " + parent.getPath());
      }
      Files.writeString(completePath, content);
      return this.toStorageNode.apply(completePath);
    });
  }

  @Override
  public Mono<StorageNode> rename(final String directoryPath, final String oldName, final String newName) {
    return fromCallable(() -> {
      final var currentPath = this.stringToPath(directoryPath);
      return this.toStorageNode.apply(Files.move(currentPath.resolve(oldName), currentPath.resolve(newName)));
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
  public Mono<StorageNode> extractZip(String path) {
    final var zipPath = this.stringToPath(path);
    return Mono.fromCallable(() -> {
      unpack(zipPath.toFile(), zipPath.getParent().toFile());
      return toStorageNode.apply(zipPath);
    });
  }

  @Override
  public Flux<StorageNode> move(List<String> paths, String destination) {
    return paste(
        paths,
        destination,
        (path, dest) -> Mono.fromCallable(() -> Files.move(path, dest, StandardCopyOption.REPLACE_EXISTING))
    );
  }

  @Override
  public Flux<StorageNode> copy(final List<String> paths, final String destination) {
    if (paths.size() == 1) {
      final var destPath = this.stringToPath(destination);
      final var path = this.stringToPath(paths.get(0));
      if (destPath.equals(path.getParent())) {
        try {
          final var fileName = "" + path.getFileName();
          String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
          final var copyPath = Paths.get(destPath.toString(), tokens[0] + "_copy." + tokens[1]);
          Files.copy(path, copyPath, StandardCopyOption.REPLACE_EXISTING);
          return Flux.just(toStorageNode.apply(copyPath));
        } catch (IOException e) {
          return Flux.error(e);
        }
      }
    }

    return paste(
        paths,
        destination,
        (path, dest) -> Mono.fromCallable(() -> {
          final var folder = Files.copy(path, dest, StandardCopyOption.REPLACE_EXISTING);
          try (final var stream = Files.walk(path)) {
            stream.forEach(subFile -> {
              try {
                Files.copy(subFile, dest.resolve(path.relativize(subFile)), StandardCopyOption.REPLACE_EXISTING);
              } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
              }
            });
          }
          return folder;
        })
    );
  }

  private Flux<StorageNode> paste(final List<String> pathsStr, final String destination, BiFunction<Path, Path, Mono<Path>> operation) {
    final var destPath = this.stringToPath(destination);
    final var paths = pathsStr.stream().map(this::stringToPath).collect(Collectors.toList());
    return Flux.fromStream(
        paths.stream()
            .filter((current) -> paths.stream().noneMatch((path) -> current.getParent().equals(path)))
            .map(path -> {
              final var name = path.toFile().getName();
              return operation.apply(path, destPath.resolve(name));
            })
            .map(Mono::block))
        .map(toStorageNode);
  }

  private InputStream downloadFolderZip(final Path path) throws IOException {
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
    return Files.newInputStream(tempPath);
  }

  private InputStream downloadFile(final Path path) throws IOException {
    return Files.newInputStream(path);
  }

  private Path stringToPath(final String path) throws IllegalArgumentException {
    checkArgument(!path.contains(".."), "Cannot store file with relative path outside current directory "
        + path);
    return root.resolve(path);
  }
}
