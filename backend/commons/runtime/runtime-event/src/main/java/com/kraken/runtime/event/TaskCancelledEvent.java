package com.kraken.runtime.event;

import com.kraken.runtime.entity.task.TaskType;
import com.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class TaskCancelledEvent implements BusEvent {

  @NonNull String applicationId;
  @NonNull String taskId;
  @NonNull TaskType type;
}
