package com.octoperf.kraken.tools.sse;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface SSEService {

  <T> Flux<ServerSentEvent<T>> keepAlive(Flux<T> flux);

  Flux<SSEWrapper> merge(Map<String, Flux<? extends Object>> fluxMap);

}
