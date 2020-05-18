package com.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TaskConfiguration {
  @NonNull TaskType type;
  @NonNull String file;
  @NonNull Integer containersCount;
  @NonNull Map<String, String> environment;

  @JsonCreator
  TaskConfiguration(
      @JsonProperty("type") final TaskType type,
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
