package com.kraken.runtime.server.event;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.ResultType;
import com.kraken.runtime.entity.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
final class TaskTypeToResultType implements Function<TaskType, ResultType> {

  private static final Map<TaskType, ResultType> MAP = ImmutableMap.of(
      TaskType.RUN, ResultType.RUN,
      TaskType.DEBUG, ResultType.DEBUG,
      TaskType.RECORD, ResultType.HAR
  );

  @Override
  public ResultType apply(TaskType taskType) {
    return MAP.get(taskType);
  }
}
