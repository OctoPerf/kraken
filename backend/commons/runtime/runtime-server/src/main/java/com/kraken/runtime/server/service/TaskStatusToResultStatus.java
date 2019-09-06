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

  private static final Map<ContainerStatus, ResultStatus> MAP = ImmutableMap.of(
      ContainerStatus.CREATING, ResultStatus.STARTING,
      ContainerStatus.STARTING, ResultStatus.STARTING,
      ContainerStatus.READY, ResultStatus.STARTING,
      ContainerStatus.RUNNING, ResultStatus.RUNNING,
      ContainerStatus.DONE, ResultStatus.COMPLETED
  );

  @Override
  public ResultStatus apply(ContainerStatus taskStatus) {
    return MAP.get(taskStatus);
  }
}
