package com.kraken.runtime.entity.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class ExecutionEnvironment {
  @NonNull String taskType;
  @NonNull String description;
  @NonNull Map<String, String> environment;
  //  Key: hostId, Value; env specific to this host
  @NonNull Map<String, Map<String, String>> hosts;

  @JsonCreator
  ExecutionEnvironment(
      @JsonProperty("description") final String description,
      @JsonProperty("taskType") final String taskType,
      @JsonProperty("environment") final Map<String, String> environment,
      @JsonProperty("hosts") final Map<String, Map<String, String>> hosts) {
    super();
    this.description = requireNonNull(description);
    this.taskType = requireNonNull(taskType);
    this.environment = requireNonNull(environment);
    this.hosts = requireNonNull(hosts);
  }
}
