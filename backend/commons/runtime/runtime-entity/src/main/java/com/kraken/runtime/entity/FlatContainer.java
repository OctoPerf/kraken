package com.kraken.runtime.entity;

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
  String containerId;
  String hostId;
  String taskId;
  TaskType taskType;
  String hostname;
  String name;
  String description;
  Long startDate;
  @With
  ContainerStatus status;
  Integer expectedCount;

  @JsonCreator
  FlatContainer(
      @JsonProperty("id") final String id,
      @JsonProperty("containerId") final String containerId,
      @JsonProperty("hostId") final String hostId,
      @JsonProperty("taskId") final String taskId,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("hostname") final String hostname,
      @JsonProperty("name") final String name,
      @JsonProperty("description") final String description,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status,
      @JsonProperty("expectedCount") final Integer expectedCount) {
    super();
    this.id = requireNonNull(id);
    this.containerId = requireNonNull(containerId);
    this.hostId = requireNonNull(hostId);
    this.taskId = requireNonNull(taskId);
    this.taskType = requireNonNull(taskType);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.hostname = requireNonNull(hostname);
    this.name = requireNonNull(name);
    this.description = requireNonNull(description);
    this.expectedCount = requireNonNull(expectedCount);
  }
}
