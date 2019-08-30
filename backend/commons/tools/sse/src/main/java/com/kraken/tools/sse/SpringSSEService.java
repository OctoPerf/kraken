package com.kraken.tools.sse;

import com.google.common.collect.ImmutableList;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringSSEService implements SSEService {

  long delay;

  @Autowired
  SpringSSEService(@Value("${kraken.sse.keep-alive:#{environment.KRAKEN_SSE_KEEP_ALIVE_DELAY ?: 15}}") final Long delay) {
    this.delay = delay;
  }

  @Override
  public <T> Flux<ServerSentEvent<T>> keepAlive(Flux<T> flux) {
    return Flux.merge(flux.map(t -> ServerSentEvent.builder(t).build()), Flux.interval(Duration.ofSeconds(this.delay)).map(aLong -> ServerSentEvent.<T>builder().comment("keep alive").build()));
  }

  @Override
  public <T> Flux<SSEWrapper<T>> merge(final String type1,
                                       final Flux<? extends T> flux1,
                                       final String type2,
                                       final Flux<? extends T> flux2) {
    final List<Flux<SSEWrapper<T>>> asEvents = ImmutableList.of(wrap(type1, flux1), wrap(type2, flux2));
    return Flux.merge(asEvents);
  }

  private <T> Flux<SSEWrapper<T>> wrap(final String type, final Flux<? extends T> flux) {
    return flux.map(t -> SSEWrapper.<T>builder().type(type).value(t).build());
  }
}
