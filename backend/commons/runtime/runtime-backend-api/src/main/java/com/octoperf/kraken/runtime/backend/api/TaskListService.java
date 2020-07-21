package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.security.entity.owner.Owner;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TaskListService {

  Flux<Task> list(Owner owner);

  Flux<List<Task>> watch(Owner owner);

}
