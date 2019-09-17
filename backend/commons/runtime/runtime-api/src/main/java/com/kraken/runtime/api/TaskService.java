package com.kraken.runtime.api;

import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface TaskService {

  Mono<String> execute(String applicationId,
                       TaskType taskType,
                       Map<String, String> environment);

  Mono<String> cancel(String applicationId,
                    Task task);

  Flux<Task> list();

  Flux<List<Task>> watch();

}
