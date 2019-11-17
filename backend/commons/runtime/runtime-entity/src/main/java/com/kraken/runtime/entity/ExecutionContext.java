package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class ExecutionContext {
  @With
  String applicationId;
  @With
  String taskId;
  TaskType taskType;
  String description;
  Map<String, String> environment;
  //  Key: hostId, Value; env specific to this host
  Map<String, Map<String, String>> hosts;

  @JsonCreator
  ExecutionContext(
      @JsonProperty("description") final String description,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("environment") final Map<String, String> environment,
      @JsonProperty("hosts") final Map<String, Map<String, String>> hosts) {
    super();
    this.applicationId = "";
    this.taskId = "";
    this.description = requireNonNull(description);
    this.taskType = requireNonNull(taskType);
    this.environment = requireNonNull(environment);
    this.hosts = requireNonNull(hosts);
  }
}
