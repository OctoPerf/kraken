package com.kraken.storage.file;

import com.kraken.storage.entity.StorageNode;
import com.kraken.config.api.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.kraken.storage.entity.StorageNodeType.*;
import static java.nio.file.Paths.get;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class FileSystemPathToStorageNode implements PathToStorageNode {

  @NonNull
  Path root;

  @Override
  public StorageNode apply(final Path path) {
    final var file = path.toFile();
    var type = NONE;
    if (file.exists()) {
      type = file.isDirectory() ? DIRECTORY : FILE;
    }
    final var relative = root.relativize(path);
    return StorageNode.builder()
        .path(relative.toString())
        .type(type)
        .length(file.length())
        .lastModified(file.lastModified())
        .depth(relative.getNameCount() - 1)
        .build();
  }
}
