package com.kraken.runtime.context.api.environment;

import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public interface EnvironmentPublisher extends EnvironmentTester, Function<ExecutionContextBuilder, Mono<List<ExecutionEnvironmentEntry>>> {
}
