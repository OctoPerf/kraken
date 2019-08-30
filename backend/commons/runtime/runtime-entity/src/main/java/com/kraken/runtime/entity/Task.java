package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Task {
  String id;
  Long startDate;
  @Wither
  TaskStatus status;
  String name;
  String description;
  TaskType type;

  @JsonCreator
  Task(
      @JsonProperty("id") final String id,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final TaskStatus status,
      @JsonProperty("name") final String name,
      @JsonProperty("description") final String description,
      @JsonProperty("type") final TaskType type) {
    super();
    this.id = requireNonNull(id);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
    this.description = requireNonNull(description);
    this.type = requireNonNull(type);
  }
}
