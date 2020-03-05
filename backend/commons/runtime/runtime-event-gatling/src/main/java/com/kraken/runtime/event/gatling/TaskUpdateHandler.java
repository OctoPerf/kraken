package com.kraken.runtime.event.gatling;

import com.kraken.runtime.entity.task.Task;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TaskUpdateHandler {

  Flux<List<Task>> scanForUpdates();

}
