package com.kraken.runtime.context.api.environment;

import com.kraken.tools.environment.KrakenEnvironmentKeys;

import java.util.Map;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public interface EnvironmentChecker extends EnvironmentTester, Consumer<Map<String, String>> {

  default void requireEnv(
    final Map<String, String> hostsEnvironment,
    final KrakenEnvironmentKeys... keys) {
    asList(keys)
      .forEach(key -> requireNonNull(hostsEnvironment.get(key.name()),
        format("Environment variable '%s' is required.", key))
      );
  }

}
