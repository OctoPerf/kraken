package com.runtime.context.environment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public interface EnvironmentChecker extends Predicate<String>, Consumer<Map<String, Map<String, String>>> {

  default void requireEnv(final Map<String, Map<String, String>> hostsEnvironment, final String... keys) {
    hostsEnvironment
        .values()
        .forEach(environment -> Arrays.asList(keys)
            .forEach(key -> requireNonNull(environment.get(key), String.format("Environment variable '%s' is required.", key))));
  }

}
