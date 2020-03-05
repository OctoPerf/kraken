package com.kraken.runtime.event;

import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskCancelledEvent implements BusEvent {
  @NonNull CancelContext context;
}
