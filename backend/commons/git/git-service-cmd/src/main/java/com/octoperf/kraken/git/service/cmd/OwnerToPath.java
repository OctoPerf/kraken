package com.octoperf.kraken.git.service.cmd;

import com.octoperf.kraken.security.entity.owner.Owner;

import java.nio.file.Path;
import java.util.function.Function;

public interface OwnerToPath extends Function<Owner, Path> {
}
