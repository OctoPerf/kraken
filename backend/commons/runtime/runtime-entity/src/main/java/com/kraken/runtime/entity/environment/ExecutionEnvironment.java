package com.kraken.runtime.entity.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class ExecutionEnvironment {
  @NonNull TaskType taskType;
  @NonNull String description;
  @NonNull List<String> hostIds;
  @NonNull List<ExecutionEnvironmentEntry> entries;

  @JsonCreator
  ExecutionEnvironment(
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("description") final String description,
      @JsonProperty("hostIds") final List<String> hostIds,
      @JsonProperty("entries") final List<ExecutionEnvironmentEntry> entries) {
    super();
    this.description = requireNonNull(description);
    this.taskType = requireNonNull(taskType);
    this.hostIds = requireNonNull(hostIds);
    this.entries = requireNonNull(entries);
  }
}
