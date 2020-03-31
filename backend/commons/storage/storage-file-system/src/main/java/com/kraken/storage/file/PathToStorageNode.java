package com.kraken.storage.file;

import com.kraken.storage.entity.StorageNode;
import com.kraken.config.api.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.function.Function;

import static com.kraken.storage.entity.StorageNodeType.*;
import static java.nio.file.Paths.get;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class PathToStorageNode implements Function<Path, StorageNode> {

  @NonNull
  ApplicationProperties kraken;

  @Override
  public StorageNode apply(final Path path) {
    final var file = path.toFile();
    var type = NONE;
    if (file.exists()) {
      type = file.isDirectory() ? DIRECTORY : FILE;
    }
    final var relative = get(kraken.getData()).relativize(path);
    return StorageNode.builder()
        .path(relative.toString())
        .type(type)
        .length(file.length())
        .lastModified(file.lastModified())
        .depth(relative.getNameCount() - 1)
        .build();
  }
}
