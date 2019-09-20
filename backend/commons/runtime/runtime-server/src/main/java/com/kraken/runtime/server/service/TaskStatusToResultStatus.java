package com.kraken.runtime.server.service;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.ContainerStatus;
import com.kraken.runtime.entity.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
final class TaskStatusToResultStatus implements Function<ContainerStatus, ResultStatus> {

  private static final Map<ContainerStatus, ResultStatus> MAP = ImmutableMap.<ContainerStatus, ResultStatus>builder()
      .put(ContainerStatus.CREATING, ResultStatus.STARTING)
      .put(ContainerStatus.STARTING, ResultStatus.STARTING)
      .put(ContainerStatus.PREPARING, ResultStatus.STARTING)
      .put(ContainerStatus.READY, ResultStatus.STARTING)
      .put(ContainerStatus.RUNNING, ResultStatus.RUNNING)
      .put(ContainerStatus.STOPPING, ResultStatus.STOPPING)
      .put(ContainerStatus.DONE, ResultStatus.COMPLETED)
      .build();

  @Override
  public ResultStatus apply(ContainerStatus taskStatus) {
    return MAP.get(taskStatus);
  }
}
