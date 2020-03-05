package com.kraken.runtime.context.api.environment;

import com.kraken.runtime.entity.task.TaskType;

import java.util.Arrays;
import java.util.function.Predicate;

public interface EnvironmentTester extends Predicate<TaskType> {
  default boolean test(final TaskType taskType, final TaskType... types) {
    return Arrays.stream(types).anyMatch(type -> type == taskType);
  }
}
