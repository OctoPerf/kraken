package com.octoperf.kraken.runtime.backend.api;

import com.octoperf.kraken.runtime.entity.task.Task;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TaskUpdateHandler {

  Flux<List<Task>> scanForUpdates();

}
