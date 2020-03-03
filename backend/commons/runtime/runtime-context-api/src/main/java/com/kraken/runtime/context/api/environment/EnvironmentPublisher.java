package com.kraken.runtime.context.api.environment;

import com.kraken.runtime.context.entity.ExecutionContextBuilder;

import java.util.function.Function;
import java.util.function.Predicate;

public interface EnvironmentPublisher extends Predicate<String>, Function<ExecutionContextBuilder, ExecutionContextBuilder> {
}
