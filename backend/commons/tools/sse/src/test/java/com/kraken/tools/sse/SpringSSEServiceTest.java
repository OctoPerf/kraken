package com.kraken.tools.sse;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class SpringSSEServiceTest {

  SSEService service;

  @Before
  public void before(){
    this.service = new SpringSSEService(1L);
  }

  @Test
  public void shouldKeepAlive(){
    final var flux = Flux.interval(Duration.ofMillis(700)).take(3)
        .map(aLong -> String.format("event%d", aLong));

    final var keepAlive = service.keepAlive(flux).take(6);

    StepVerifier
        .create(keepAlive)
        .expectNextMatches(sse -> "event0".equals(sse.data()))
        .expectNextMatches(sse -> "keep alive".equals(sse.comment()))
        .expectNextMatches(sse -> "event1".equals(sse.data()))
        .expectNextMatches(sse -> "keep alive".equals(sse.comment()))
        .expectNextMatches(sse -> "event2".equals(sse.data()))
        .expectNextMatches(sse -> "keep alive".equals(sse.comment()))
        .expectComplete()
        .verify();
  }

  @Test
  public void shouldMerge(){
    final var strFlux = Flux.interval(Duration.ofMillis(500)).take(3)
        .map(aLong -> String.format("event%d", aLong));
    final var longFlux = Flux.interval(Duration.ofMillis(700)).take(3);

    final var merged = service.<Object>merge("String", strFlux, "Long", longFlux ).take(6);

    StepVerifier
        .create(merged)
        .expectNext(SSEWrapper.builder().type("String").value("event0").build())
        .expectNext(SSEWrapper.builder().type("Long").value(0L).build())
        .expectNext(SSEWrapper.builder().type("String").value("event1").build())
        .expectNext(SSEWrapper.builder().type("Long").value(1L).build())
        .expectNext(SSEWrapper.builder().type("String").value("event2").build())
        .expectNext(SSEWrapper.builder().type("Long").value(2L).build())
        .expectComplete()
        .verify();
  }
}
