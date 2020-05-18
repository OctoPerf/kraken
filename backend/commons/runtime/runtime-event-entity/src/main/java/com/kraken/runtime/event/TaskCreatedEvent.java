package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.Task;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TaskCreatedEvent implements TaskEvent {
  @NonNull Task task;

  @JsonCreator
  TaskCreatedEvent(
      @JsonProperty("task") final Task task
  ) {
    super();
    this.task = requireNonNull(task);
  }
}
