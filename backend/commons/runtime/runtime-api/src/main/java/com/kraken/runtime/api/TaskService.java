package com.kraken.runtime.api;

import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

  Mono<ExecutionContext> execute(ExecutionContext context);

  Mono<String> cancel(String applicationId, String taskId, TaskType taskType);

  Flux<FlatContainer> list();

}
