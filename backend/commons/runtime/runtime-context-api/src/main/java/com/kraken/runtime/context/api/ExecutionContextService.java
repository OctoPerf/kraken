package com.kraken.runtime.context.api;

import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.context.entity.ExecutionContext;
import reactor.core.publisher.Mono;

public interface ExecutionContextService {

  Mono<ExecutionContext> newExecuteContext(String applicationId, ExecutionEnvironment environment);

  Mono<CancelContext> newCancelContext(String applicationId, String taskId, String taskType);

}
