package com.kraken.runtime.backend.api;

import com.kraken.runtime.entity.task.FlatContainer;
import com.runtime.context.entity.CancelContext;
import com.runtime.context.entity.ExecutionContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TaskService {

  Mono<ExecutionContext> execute(ExecutionContext context);

  Mono<CancelContext> cancel(CancelContext context);

  Mono<CancelContext> remove(CancelContext context);

  Flux<FlatContainer> list(Optional<String> applicationId);

}
