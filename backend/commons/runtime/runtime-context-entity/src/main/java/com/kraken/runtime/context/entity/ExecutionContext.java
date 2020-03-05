package com.kraken.runtime.context.entity;

import com.kraken.runtime.entity.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
@AllArgsConstructor
public class ExecutionContext {
  @NonNull String applicationId;
  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String description;
  //  Key: hostId, Value; template specific to this host
  @NonNull Map<String, String> templates;
}
