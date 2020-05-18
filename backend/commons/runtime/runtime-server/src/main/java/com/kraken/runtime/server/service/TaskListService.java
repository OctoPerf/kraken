package com.kraken.runtime.server.service;

import com.kraken.runtime.entity.task.Task;
import com.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface TaskListService {

  Flux<Task> list(Owner owner);

  Flux<List<Task>> watch(Owner owner);

}
