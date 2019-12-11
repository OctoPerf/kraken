package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TaskUpdateHandler {

  Mono<String> taskExecuted(ExecutionContext context);

  Mono<String> taskCanceled(String taskId);

  Flux<List<Task>> scanForUpdates();

  Mono<Void> taskCreated(final Task task);

  Mono<Void> taskStatusUpdated(final Task task);

  Mono<Void> taskRemoved(final Task task);

}
