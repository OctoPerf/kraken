package com.kraken.tools.sse;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public interface SSEService {

  <T> Flux<ServerSentEvent<T>> keepAlive(Flux<T> flux);

  Flux<SSEWrapper> merge(String type1, Flux<? extends Object> flux1, String type2, Flux<? extends Object> flux2);

}
