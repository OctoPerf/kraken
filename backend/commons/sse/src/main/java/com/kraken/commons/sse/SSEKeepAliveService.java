package com.kraken.commons.sse;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SSEKeepAliveService implements SSEService {

  long delay;

  @Autowired
  SSEKeepAliveService(@Value("${kraken.sse.keep-alive:#{environment.KRAKEN_SSE_KEEP_ALIVE_DELAY ?: 5}}") final Long delay) {
    this.delay = delay;
  }

  @Override
  public <T> Flux<ServerSentEvent<T>> wrap(Flux<T> flux) {
    return Flux.merge(flux.map(t -> ServerSentEvent.builder(t).build()), Flux.interval(Duration.ofSeconds(this.delay)).map(aLong -> ServerSentEvent.<T>builder().comment("keep alive").build()));
  }
}
