package com.kraken.runtime.container;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.Task;
import com.kraken.runtime.entity.TaskType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface ContainerService {

  void execute(TaskType taskType,
               Map<String, String> environment);

  Flux<Task> list();

  Flux<List<Task>> tasks();

  Flux<Log> logs();

  void attachLogs(String id);

  void detachLogs(String id);

}
