package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.context.entity.CancelContext;
import com.octoperf.kraken.runtime.context.entity.ExecutionContext;
import com.octoperf.kraken.runtime.entity.task.FlatContainer;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

  Mono<ExecutionContext> execute(ExecutionContext context);

  Mono<CancelContext> cancel(CancelContext context);

  Mono<CancelContext> remove(CancelContext context);

  Flux<FlatContainer> list(Owner owner);

}
