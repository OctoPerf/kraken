package com.octoperf.kraken.analysis.task.events.listener;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.analysis.entity.ResultStatus;
import com.octoperf.kraken.runtime.entity.task.ContainerStatus;
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
      .put(ContainerStatus.FAILED, ResultStatus.FAILED)
      .build();

  @Override
  public ResultStatus apply(ContainerStatus taskStatus) {
    return MAP.get(taskStatus);
  }
}
