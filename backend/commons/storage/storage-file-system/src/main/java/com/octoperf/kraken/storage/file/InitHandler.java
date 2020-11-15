package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageInitMode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;

public interface InitHandler extends Predicate<StorageInitMode> {
  void init(final Path root, final Path applicationPath) throws IOException;
}
