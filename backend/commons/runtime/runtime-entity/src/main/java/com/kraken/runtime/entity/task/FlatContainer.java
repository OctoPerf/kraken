package com.kraken.runtime.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class FlatContainer {
  String id;
  String name;
  String hostId;
  String taskId;
  TaskType taskType;
  String label;
  String description;
  Long startDate;
  @With
  ContainerStatus status;
  Integer expectedCount;
  String applicationId;

  @JsonCreator
  FlatContainer(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("hostId") final String hostId,
      @JsonProperty("taskId") final String taskId,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("label") final String label,
      @JsonProperty("description") final String description,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status,
      @JsonProperty("expectedCount") final Integer expectedCount,
      @JsonProperty("applicationId") final String applicationId) {
    super();
    this.id = requireNonNull(id);
    this.name = requireNonNull(name);
    this.hostId = requireNonNull(hostId);
    this.taskId = requireNonNull(taskId);
    this.taskType = requireNonNull(taskType);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.label = requireNonNull(label);
    this.description = requireNonNull(description);
    this.expectedCount = requireNonNull(expectedCount);
    this.applicationId = requireNonNull(applicationId);
  }
}
