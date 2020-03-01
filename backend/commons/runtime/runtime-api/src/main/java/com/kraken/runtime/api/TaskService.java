package com.kraken.runtime.api;

import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.task.FlatContainer;
import com.kraken.runtime.entity.task.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TaskService {

  Mono<ExecutionContext> execute(ExecutionContext context);

  Mono<String> cancel(String applicationId, String taskId, TaskType taskType);

  Mono<String> remove(String applicationId, String taskId, TaskType taskType);

  Flux<FlatContainer> list(Optional<String> applicationId);

}
