package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.Task;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)

public class TaskStatusUpdatedEvent implements TaskEvent {
  @NonNull Task task;
  @NonNull ContainerStatus previousStatus;

  @JsonCreator
  TaskStatusUpdatedEvent(
      @JsonProperty("task") final Task task,
      @JsonProperty("previousStatus") final ContainerStatus previousStatus
  ) {
    super();
    this.task = requireNonNull(task);
    this.previousStatus = requireNonNull(previousStatus);
  }
}
