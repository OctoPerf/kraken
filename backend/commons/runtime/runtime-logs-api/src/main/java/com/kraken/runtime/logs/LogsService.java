package com.kraken.runtime.logs;

import com.kraken.runtime.entity.log.Log;
import com.kraken.runtime.entity.log.LogType;
import com.kraken.security.entity.owner.Owner;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public interface LogsService {

  Flux<Log> listen(Owner owner);

  boolean dispose(Owner owner, String id, LogType type);

  Disposable push(Owner owner, String id, LogType type, Flux<String> logs);

  void add(Log log);

  void clear();

}
