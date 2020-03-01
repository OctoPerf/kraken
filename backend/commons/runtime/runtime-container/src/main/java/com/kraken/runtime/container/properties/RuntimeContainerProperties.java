package com.kraken.runtime.container.properties;

import com.kraken.runtime.entity.task.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RuntimeContainerProperties {

  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String containerName;
  @NonNull String hostId;

}
