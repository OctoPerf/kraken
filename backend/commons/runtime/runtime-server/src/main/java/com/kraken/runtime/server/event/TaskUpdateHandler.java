package com.kraken.runtime.server.event;

import com.kraken.runtime.entity.Task;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TaskUpdateHandler {

  Flux<List<Task>> scanForUpdates();

}
