package com.kraken.command.client;

import com.kraken.command.entity.Command;
import reactor.core.publisher.Mono;

public interface CommandClient {
  Mono<String> execute(Command command);
}
