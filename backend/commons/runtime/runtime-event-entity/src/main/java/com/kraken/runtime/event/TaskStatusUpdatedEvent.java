package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.Task;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)

public class TaskStatusUpdatedEvent implements TaskEvent {
  Task task;
  ContainerStatus previousStatus;

  @JsonCreator
  TaskStatusUpdatedEvent(
      @NonNull @JsonProperty("task") final Task task,
      @NonNull @JsonProperty("previousStatus") final ContainerStatus previousStatus
  ) {
    super();
    this.task = task;
    this.previousStatus = previousStatus;
  }
}
