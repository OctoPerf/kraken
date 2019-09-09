package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Mono;

public interface ResultUpdater {

  Mono<String> taskExecuted(String taskId, TaskType taskType, String description);

  Mono<String> taskCanceled(String taskId);

}
