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
public class CancelContext {
  @NonNull String applicationId;
  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String template;
}
