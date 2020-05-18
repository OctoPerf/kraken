package com.kraken.analysis.task.events.watcher;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.task.TaskType;
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
