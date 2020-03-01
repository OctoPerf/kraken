package com.kraken.runtime.entity.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class TaskConfiguration {
  @NonNull String file;
  @NonNull Integer containersCount;
  @NonNull Map<String, String> environment;

  @JsonCreator
  TaskConfiguration(
      @JsonProperty("file") final String file,
      @JsonProperty("containers-count") final Integer containersCount,
      @JsonProperty("environment") final Map<String, String> environment) {
    super();
    this.file = requireNonNull(file);
    this.containersCount = requireNonNull(containersCount);
    this.environment = requireNonNull(environment);
  }
}
