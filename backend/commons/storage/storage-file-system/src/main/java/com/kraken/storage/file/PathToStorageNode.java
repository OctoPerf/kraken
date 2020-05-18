package com.kraken.storage.file;

import com.kraken.storage.entity.StorageNode;

import java.nio.file.Path;
import java.util.function.Function;

public interface PathToStorageNode extends Function<Path, StorageNode> {
}
