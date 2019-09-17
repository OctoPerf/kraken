package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ResultUpdater {

  Mono<String> taskExecuted(String taskId, TaskType taskType, Map<String, String> environment);

  Mono<String> taskCanceled(String taskId);

}
