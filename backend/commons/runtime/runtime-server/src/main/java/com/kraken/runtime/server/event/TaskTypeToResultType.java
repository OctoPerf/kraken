package com.kraken.runtime.server.event;

import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.entity.ResultType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
final class TaskTypeToResultType implements Function<String, ResultType> {

  private static final Map<String, ResultType> MAP = ImmutableMap.of(
      "RUN", ResultType.RUN,
      "DEBUG", ResultType.DEBUG,
      "RECORD", ResultType.HAR
  );

  @Override
  public ResultType apply(String taskType) {
    return MAP.get(taskType);
  }
}
