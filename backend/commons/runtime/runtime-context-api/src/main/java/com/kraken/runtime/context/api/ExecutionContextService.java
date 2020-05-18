package com.kraken.runtime.context.api;

import com.kraken.runtime.context.entity.CancelContext;
import com.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.kraken.runtime.context.entity.ExecutionContext;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface ExecutionContextService {

  Mono<ExecutionContext> newExecuteContext(Owner owner, ExecutionEnvironment environment);

  Mono<CancelContext> newCancelContext(Owner owner, String taskId, TaskType taskType);

}
