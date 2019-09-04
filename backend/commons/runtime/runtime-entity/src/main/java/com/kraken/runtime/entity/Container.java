package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Container {
  String id;
  String containerId;
  String taskId;
  TaskType taskType;
  String name;
  String description;
// TODO add String hostname;
  Long startDate;
  @Wither
  ContainerStatus status;

  @JsonCreator
  Container(
      @JsonProperty("id") final String id,
      @JsonProperty("containerId") final String containerId,
      @JsonProperty("taskId") final String taskId,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("name") final String name,
      @JsonProperty("description") final String description,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = requireNonNull(id);
    this.containerId = requireNonNull(containerId);
    this.taskId = requireNonNull(taskId);
    this.taskType = requireNonNull(taskType);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
    this.description = requireNonNull(description);
  }
}
