package com.kraken.runtime.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.security.entity.owner.Owned;
import com.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class FlatContainer implements Owned {
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
  Owner owner;

  @JsonCreator
  FlatContainer(
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("hostId") final String hostId,
      @NonNull @JsonProperty("taskId") final String taskId,
      @NonNull @JsonProperty("taskType") final TaskType taskType,
      @NonNull @JsonProperty("label") final String label,
      @NonNull @JsonProperty("description") final String description,
      @NonNull @JsonProperty("startDate") final Long startDate,
      @NonNull @JsonProperty("status") final ContainerStatus status,
      @NonNull @JsonProperty("expectedCount") final Integer expectedCount,
      @NonNull @JsonProperty("owner") final Owner owner) {
    super();
    this.id = id;
    this.name = name;
    this.hostId = hostId;
    this.taskId = taskId;
    this.taskType = taskType;
    this.startDate = startDate;
    this.status = status;
    this.label = label;
    this.description = description;
    this.expectedCount = expectedCount;
    this.owner = owner;
  }
}
