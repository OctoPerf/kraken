package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.ExecutionContext;
import reactor.core.publisher.Mono;

public interface ResultUpdater {

  Mono<String> taskExecuted(ExecutionContext context);

  Mono<String> taskCanceled(String taskId);

}
