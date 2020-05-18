package com.kraken.runtime.tasks.configuration.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
public class TaskConfiguration {
  TaskType type;
  String file;
  Integer containersCount;
  Map<String, String> environment;

  @JsonCreator
  TaskConfiguration(
      @NonNull @JsonProperty("type") final TaskType type,
      @NonNull @JsonProperty("file") final String file,
      @NonNull @JsonProperty("containers-count") final Integer containersCount,
      @NonNull @JsonProperty("environment") final Map<String, String> environment) {
    super();
    this.type = type;
    this.file = file;
    this.containersCount = containersCount;
    this.environment = environment;
  }
}
