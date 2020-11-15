package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageInitMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

@Component
final class CopyInitHandler implements InitHandler {
  @Override
  public void init(final Path root, final Path applicationPath) throws IOException {
    try (final var stream = Files.walk(applicationPath)) {
      final var files = stream.collect(Collectors.toUnmodifiableList());
      for (var file : files) {
        Files.copy(file, root.resolve(applicationPath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
      }
    }
  }

  @Override
  public boolean test(final StorageInitMode mode) {
    return StorageInitMode.COPY.equals(mode);
  }
}
