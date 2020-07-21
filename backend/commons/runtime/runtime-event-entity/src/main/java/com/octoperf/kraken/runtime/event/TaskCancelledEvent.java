package com.octoperf.kraken.runtime.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.runtime.context.entity.CancelContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class TaskCancelledEvent implements TaskEvent {
  @NonNull CancelContext context;

  @JsonCreator
  TaskCancelledEvent(
      @NonNull @JsonProperty("context") final CancelContext context
  ) {
    super();
    this.context = context;
  }
}
