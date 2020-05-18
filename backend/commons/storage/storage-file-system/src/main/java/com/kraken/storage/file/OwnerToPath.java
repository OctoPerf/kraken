package com.kraken.storage.file;

import com.kraken.security.entity.owner.Owner;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface OwnerToPath extends Function<Owner, Path> {
}
