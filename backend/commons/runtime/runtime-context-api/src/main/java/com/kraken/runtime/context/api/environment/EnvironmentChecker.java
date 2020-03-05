package com.kraken.runtime.context.api.environment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public interface EnvironmentChecker extends EnvironmentTester, Consumer<Map<String, String>> {

  default void requireEnv(final Map<String, String> hostsEnvironment, final String... keys) {
    Arrays.asList(keys).forEach(key -> requireNonNull(hostsEnvironment.get(key), String.format("Environment variable '%s' is required.", key)));
  }

}
