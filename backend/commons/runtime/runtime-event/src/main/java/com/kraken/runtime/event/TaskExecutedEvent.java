package com.kraken.runtime.event;

import com.kraken.tools.event.bus.BusEvent;
import com.runtime.context.entity.ExecutionContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskExecutedEvent implements BusEvent {
  @NonNull ExecutionContext context;
}
