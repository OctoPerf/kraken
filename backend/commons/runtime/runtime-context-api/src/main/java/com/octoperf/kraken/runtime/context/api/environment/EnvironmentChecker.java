package com.octoperf.kraken.runtime.context.api.environment;

import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.time.Duration.parse;
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

  default Boolean getBoolean(final Map<String, String> environment, final KrakenEnvironmentKeys key) {
    return parseBoolean(environment.getOrDefault(key.name(), "false"));
  }

  default Long getLong(final Map<String, String> environment, final KrakenEnvironmentKeys key) {
    return parseLong(requireNonNull(environment.get(key.name())));
  }

  default Duration getDuration(final Map<String, String> environment, final KrakenEnvironmentKeys key) {
    return parse(requireNonNull(environment.get(key.name())));
  }
}
