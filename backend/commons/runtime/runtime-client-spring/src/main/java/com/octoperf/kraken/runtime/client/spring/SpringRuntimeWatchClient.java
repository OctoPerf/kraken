package com.octoperf.kraken.runtime.client.spring;

import com.octoperf.kraken.runtime.backend.api.TaskListService;
import com.octoperf.kraken.runtime.client.api.RuntimeWatchClient;
import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.runtime.logs.LogsService;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
final class SpringRuntimeWatchClient implements RuntimeWatchClient {

  @NonNull Owner owner;
  @NonNull LogsService logsService;
  @NonNull TaskListService taskListService;

  @Override
  public Flux<Log> watchLogs() {
    return logsService.listen(owner);
  }

  @Override
  public Flux<List<Task>> watchTasks() {
    return taskListService.watch(owner);
  }
}
