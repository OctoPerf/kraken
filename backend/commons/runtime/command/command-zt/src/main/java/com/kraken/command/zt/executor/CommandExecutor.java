package com.kraken.command.zt.executor;

import com.kraken.command.entity.Command;
import com.kraken.command.entity.CommandLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommandExecutor {

  Mono<String> execute(Command command);

  Flux<CommandLog> listen(String applicationId);

  boolean cancel(String commandId);

  void clear();
}
