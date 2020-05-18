package com.kraken.security.entity.functions.api;

import com.kraken.security.entity.owner.Owner;

import java.util.Optional;
import java.util.function.Function;

public interface OwnerToUserId extends Function<Owner, Optional<String>> {
}
