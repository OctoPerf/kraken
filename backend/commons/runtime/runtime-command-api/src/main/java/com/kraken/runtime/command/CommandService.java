package com.kraken.runtime.command;

import reactor.core.publisher.Flux;

public interface CommandService {

  Flux<String> execute(Command command);

}
