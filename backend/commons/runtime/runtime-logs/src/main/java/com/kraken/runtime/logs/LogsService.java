package com.kraken.runtime.logs;

import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.LogType;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public interface LogsService {

  Flux<Log> listen(String applicationId);

  boolean cancel(String id);

  Disposable push(String applicationId, String id, LogType type, Flux<String> logs);

  void clear();

}
