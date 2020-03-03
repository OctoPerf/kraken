package com.kraken.runtime.context.entity;

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
  @NonNull String taskType;
  //  Key: hostId, Value; template specific to this host
  @NonNull Map<String, String> templates;
}
