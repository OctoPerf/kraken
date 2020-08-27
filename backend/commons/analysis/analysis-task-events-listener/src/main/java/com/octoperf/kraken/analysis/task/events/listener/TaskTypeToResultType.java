package com.octoperf.kraken.analysis.task.events.listener;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.analysis.entity.ResultType;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
final class TaskTypeToResultType implements Function<TaskType, ResultType> {

  private static final Map<TaskType, ResultType> MAP = ImmutableMap.of(
      TaskType.GATLING_RUN, ResultType.RUN,
      TaskType.GATLING_DEBUG, ResultType.DEBUG,
      TaskType.GATLING_RECORD, ResultType.HAR
  );

  @Override
  public ResultType apply(TaskType taskType) {
    return MAP.get(taskType);
  }
}
