package com.kraken.runtime.logs;

import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.LogType;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public interface LogsService {

  Flux<String> concat(Flux<String> logs);

  Flux<Log> listen(String applicationId);

  boolean cancel(String id);

  Disposable push(String applicationId, String id, LogType type, Flux<String> logs);

  Disposable push(Flux<Log> logFlux);

  void clear();

}
