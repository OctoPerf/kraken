package com.kraken.runtime.docker.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class DockerContainer {
  String id;
  String containerId;
  String hostId;
  String taskId;
  TaskType taskType;
  String name;
  String description;
  Long startDate;
  @With
  ContainerStatus status;

  @JsonCreator
  DockerContainer(
      @JsonProperty("id") final String id,
      @JsonProperty("containerId") final String containerId,
      @JsonProperty("hostId") final String hostId,
      @JsonProperty("taskId") final String taskId,
      @JsonProperty("taskType") final TaskType taskType,
      @JsonProperty("name") final String name,
      @JsonProperty("description") final String description,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = requireNonNull(id);
    this.containerId = requireNonNull(containerId);
    this.hostId = requireNonNull(hostId);
    this.taskId = requireNonNull(taskId);
    this.taskType = requireNonNull(taskType);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
    this.description = requireNonNull(description);
  }
}
