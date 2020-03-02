package com.kraken.runtime.event;

import com.kraken.tools.event.bus.BusEvent;
import com.runtime.context.entity.CancelContext;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskCancelledEvent implements BusEvent {
  @NonNull CancelContext context;
}
