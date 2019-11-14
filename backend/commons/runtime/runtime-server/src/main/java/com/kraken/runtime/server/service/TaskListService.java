package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface TaskListService {

  Flux<Task> list();

  Flux<List<Task>> watch();

}
