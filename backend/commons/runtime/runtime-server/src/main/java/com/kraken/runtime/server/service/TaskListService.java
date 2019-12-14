package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskListService {

  Flux<Task> list(Optional<String> applicationId);

  Flux<List<Task>> watch(Optional<String> applicationId);

}
