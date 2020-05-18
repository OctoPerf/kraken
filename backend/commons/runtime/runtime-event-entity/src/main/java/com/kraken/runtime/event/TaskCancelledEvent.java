package com.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.context.entity.CancelContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class TaskCancelledEvent implements TaskEvent {
  @NonNull CancelContext context;

  @JsonCreator
  TaskCancelledEvent(
      @JsonProperty("context") final CancelContext context
  ) {
    super();
    this.context = requireNonNull(context);
  }
}
