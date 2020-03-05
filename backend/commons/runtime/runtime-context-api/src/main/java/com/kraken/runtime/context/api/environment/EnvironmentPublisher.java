package com.kraken.runtime.context.api.environment;

import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.task.TaskType;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EnvironmentPublisher extends EnvironmentTester, Function<ExecutionContextBuilder, ExecutionContextBuilder> {

}
