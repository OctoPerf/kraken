package com.kraken.runtime.event;

import com.kraken.runtime.entity.task.ContainerStatus;
import com.kraken.runtime.entity.task.Task;
import com.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskStatusUpdatedEvent implements BusEvent {
  @NonNull Task task;
  @NonNull ContainerStatus previousStatus;
}
