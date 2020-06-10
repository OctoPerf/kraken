package com.octoperf.kraken.runtime.client.api;

import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.Task;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClient;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RuntimeWatchClient extends AuthenticatedClient {

  Flux<Log> watchLogs();

  Flux<List<Task>> watchTasks();

}
