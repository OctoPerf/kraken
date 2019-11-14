package com.kraken.runtime.api;

import com.kraken.runtime.entity.FlatContainer;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public interface TaskService {

  Mono<String> execute(String applicationId,
                       TaskType taskType,
                       Integer replicas,
                       Map<String, String> environment);

  Mono<String> cancel(String applicationId, String taskId, TaskType taskType);

  Flux<FlatContainer> list();

  Mono<Integer> hostsCount();

}
