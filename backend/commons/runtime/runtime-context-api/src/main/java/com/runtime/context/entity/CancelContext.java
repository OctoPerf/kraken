package com.runtime.context.entity;

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
  @NonNull String taskType;
  @NonNull String file;
}
