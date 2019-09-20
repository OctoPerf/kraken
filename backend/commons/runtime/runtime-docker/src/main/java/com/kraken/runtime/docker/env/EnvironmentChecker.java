package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public interface EnvironmentChecker extends Predicate<TaskType>, Consumer<Map<String, String>> {

  default void requireEnv(final Map<String, String> environment, final String... keys) {
    Arrays.asList(keys).forEach(key -> requireNonNull(environment.get(key), String.format("Environment variable '%s' is required.", key)));
  }

}
