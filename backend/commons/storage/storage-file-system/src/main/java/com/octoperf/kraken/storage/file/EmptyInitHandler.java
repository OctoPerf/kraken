package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageInitMode;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
final class EmptyInitHandler implements InitHandler {
  @Override
  public void init(final Path root, final Path applicationPath) {
    // Does nothing
  }

  @Override
  public boolean test(final StorageInitMode mode) {
    return StorageInitMode.EMPTY.equals(mode);
  }
}
