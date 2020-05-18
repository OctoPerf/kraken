package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.Task;
import com.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TaskRemovedEvent implements TaskEvent {
  Task task;

  @JsonCreator
  TaskRemovedEvent(
      @JsonProperty("task") final Task task
  ) {
    super();
    this.task = requireNonNull(task);
  }
}
