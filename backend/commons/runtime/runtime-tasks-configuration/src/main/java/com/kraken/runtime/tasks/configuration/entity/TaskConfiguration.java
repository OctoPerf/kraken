package com.kraken.runtime.tasks.configuration.entity;

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
  @NonNull String type;
  @NonNull String file;
  @NonNull Integer containersCount;
  @NonNull Map<String, String> environment;

  @JsonCreator
  TaskConfiguration(
      @JsonProperty("type") final String type,
      @JsonProperty("file") final String file,
      @JsonProperty("containers-count") final Integer containersCount,
      @JsonProperty("environment") final Map<String, String> environment) {
    super();
    this.type = requireNonNull(type);
    this.file = requireNonNull(file);
    this.containersCount = requireNonNull(containersCount);
    this.environment = requireNonNull(environment);
  }
}
