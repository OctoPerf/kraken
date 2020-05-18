package com.kraken.runtime.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.security.entity.owner.Owned;
import com.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Task implements Owned {
  String id;
  Long startDate;
  ContainerStatus status;
  TaskType type;
  List<Container> containers;
  Integer expectedCount;
  String description;
  Owner owner;

  @JsonCreator
  Task(
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("startDate") final Long startDate,
      @NonNull @JsonProperty("status") final ContainerStatus status,
      @NonNull @JsonProperty("type") final TaskType type,
      @NonNull @JsonProperty("containers") final List<Container> containers,
      @NonNull @JsonProperty("expectedCount") final Integer expectedCount,
      @NonNull @JsonProperty("description") final String description,
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.id = id;
    this.startDate = startDate;
    this.status = status;
    this.type = type;
    this.containers = containers;
    this.expectedCount = expectedCount;
    this.description = description;
    this.owner = owner;
  }
}
