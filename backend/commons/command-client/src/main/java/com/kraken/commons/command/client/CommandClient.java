package com.kraken.commons.command.client;

import com.kraken.commons.command.entity.Command;
import reactor.core.publisher.Mono;

public interface CommandClient {
  Mono<String> execute(Command command);
}
