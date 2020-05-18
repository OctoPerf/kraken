package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.context.entity.ExecutionContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class TaskExecutedEvent implements TaskEvent {
  @NonNull ExecutionContext context;

  @JsonCreator
  TaskExecutedEvent(
      @NonNull @JsonProperty("context") final ExecutionContext context
  ) {
    super();
    this.context = context;
  }
}
