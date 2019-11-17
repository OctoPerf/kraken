package com.kraken.runtime.kubernetes.deployment;

import com.kraken.runtime.entity.TaskType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class DeploymentContext {

  @NonNull String taskId;
  @NonNull String applicationId;
  @NonNull TaskType taskType;
  @NonNull Map<String, String> environment;
  @NonNull Integer replicas;
}
