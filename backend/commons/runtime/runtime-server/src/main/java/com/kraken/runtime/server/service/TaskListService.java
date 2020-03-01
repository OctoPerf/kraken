package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.task.Task;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface TaskListService {

  Flux<Task> list(Optional<String> applicationId);

  Flux<List<Task>> watch(Optional<String> applicationId);

}
