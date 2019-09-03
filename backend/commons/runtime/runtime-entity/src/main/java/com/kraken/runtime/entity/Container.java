package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Container {
  String id;
  String taskId;
  TaskType taskType;
  String name;
  Long startDate;
  ContainerStatus status;

  @JsonCreator
  Container(
      @JsonProperty("id") final String id,
      @JsonProperty("taskId") final String taskId,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("name") final String name,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = requireNonNull(id);
    this.taskId = requireNonNull(taskId);
    this.taskType = requireNonNull(taskType);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
  }
}
