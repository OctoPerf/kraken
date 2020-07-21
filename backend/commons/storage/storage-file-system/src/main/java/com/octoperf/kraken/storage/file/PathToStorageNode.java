package com.octoperf.kraken.storage.file;

import com.octoperf.kraken.storage.entity.StorageNode;

import java.nio.file.Path;
import java.util.function.Function;

public interface PathToStorageNode extends Function<Path, StorageNode> {
}
