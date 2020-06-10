package com.octoperf.kraken.runtime.context.api.environment;

import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public interface EnvironmentPublisher extends EnvironmentTester, Function<ExecutionContextBuilder, Mono<List<ExecutionEnvironmentEntry>>> {
}
