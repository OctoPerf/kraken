package com.kraken.storage.file;

import com.kraken.storage.entity.StorageNode;
import com.kraken.storage.entity.StorageNodeType;
import com.kraken.tools.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.function.Function;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class PathToStorageNode implements Function<Path, StorageNode> {

  @NonNull
  ApplicationProperties applicationProperties;

  @Override
  public StorageNode apply(Path path) {
    final var file = path.toFile();
    var type = StorageNodeType.NONE;
    if (file.exists()) {
      type = file.isDirectory() ? StorageNodeType.DIRECTORY : StorageNodeType.FILE;
    }
    final var relative = this.applicationProperties.getData().relativize(path);
    return StorageNode.builder()
        .path(relative.toString())
        .type(type)
        .length(file.length())
        .lastModified(file.lastModified())
        .depth(relative.getNameCount() - 1)
        .build();
  }
}
