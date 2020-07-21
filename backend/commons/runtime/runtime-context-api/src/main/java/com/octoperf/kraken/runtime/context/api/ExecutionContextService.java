package com.octoperf.kraken.runtime.context.api;

import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironment;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Mono;

public interface ExecutionContextService {

  Mono<ExecutionContext> newExecuteContext(Owner owner, ExecutionEnvironment environment);

  Mono<CancelContext> newCancelContext(Owner owner, String taskId, TaskType taskType);

}
