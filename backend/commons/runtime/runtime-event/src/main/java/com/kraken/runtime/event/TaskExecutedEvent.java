package com.kraken.runtime.event;

import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskExecutedEvent implements BusEvent {
  @NonNull ExecutionContext context;
}
