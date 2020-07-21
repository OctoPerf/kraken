package com.octoperf.kraken.tools.webclient;

import reactor.core.publisher.Mono;

public interface ClientBuilder<T extends Client> {
  Mono<T> build();
}
