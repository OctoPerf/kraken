package com.octoperf.kraken.tools.sse;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toUnmodifiableList;
import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Flux.interval;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringSSEService implements SSEService {

  long delay;

  SpringSSEService(@Value("${kraken.sse.keep-alive:15}") final Long delay) {
    super();
    this.delay = delay;
  }

  @Override
  public <T> Flux<ServerSentEvent<T>> keepAlive(final Flux<T> flux) {
    return Flux
      .merge(
        flux
          .map(t -> ServerSentEvent.builder(t).build()), interval(ofSeconds(this.delay))
          .map(aLong -> ServerSentEvent.<T>builder().comment("keep alive")
            .build()
          )
      );
  }

  @Override
  public Flux<SSEWrapper> merge(final Map<String, Flux<? extends Object>> fluxMap) {
    final List<Flux<SSEWrapper>> asEvents = fluxMap
      .entrySet()
      .stream()
      .map(entry -> wrap(entry.getKey(), entry.getValue()))
      .collect(toUnmodifiableList());
    return Flux.merge(asEvents);
  }

  private <T> Flux<SSEWrapper> wrap(final String type, final Flux<? extends T> flux) {
    return flux.map(t -> SSEWrapper.builder().type(type).value(t).build());
  }
}
