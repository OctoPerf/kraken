package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.TaskType;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface EnvironmentPublisher extends Predicate<TaskType>, Function<ExecutionContext, Map<String, String>> {
}
