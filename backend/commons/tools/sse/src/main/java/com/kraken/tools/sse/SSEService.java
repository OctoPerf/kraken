package com.kraken.tools.sse;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public interface SSEService {

  <T> Flux<ServerSentEvent<T>> keepAlive(Flux<T> flux);

  <T> Flux<SSEWrapper<T>> merge(String type1, Flux<? extends T> flux1, String type2, Flux<? extends T> flux2);

}
