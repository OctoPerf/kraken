package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.context.entity.ExecutionContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TaskExecutedEvent implements TaskEvent {
  @NonNull ExecutionContext context;

  @JsonCreator
  TaskExecutedEvent(
      @JsonProperty("context") final ExecutionContext context
  ) {
    super();
    this.context = requireNonNull(context);
  }
}
