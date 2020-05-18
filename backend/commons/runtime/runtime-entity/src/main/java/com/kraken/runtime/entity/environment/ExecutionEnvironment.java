package com.kraken.runtime.entity.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ExecutionEnvironment {
  @NonNull TaskType taskType;
  @NonNull String description;
  @NonNull List<String> hostIds;
  @NonNull List<ExecutionEnvironmentEntry> entries;

  @JsonCreator
  ExecutionEnvironment(
      @NonNull @JsonProperty("taskType") final TaskType taskType,
      @NonNull @JsonProperty("description") final String description,
      @NonNull @JsonProperty("hostIds") final List<String> hostIds,
      @NonNull @JsonProperty("entries") final List<ExecutionEnvironmentEntry> entries) {
    super();
    this.description = description;
    this.taskType = taskType;
    this.hostIds = hostIds;
    this.entries = entries;
  }
}
