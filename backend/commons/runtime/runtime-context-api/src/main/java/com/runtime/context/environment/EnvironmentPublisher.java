package com.runtime.context.environment;

import com.runtime.context.entity.ExecutionContext;

import java.util.function.Function;
import java.util.function.Predicate;

public interface EnvironmentPublisher extends Predicate<String>, Function<ExecutionContext, ExecutionContext> {
}
