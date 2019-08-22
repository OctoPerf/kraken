package com.kraken.commons.sse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class SSEKeepAliveServiceTest {

  SSEService service;

  @Before
  public void before(){
    this.service = new SSEKeepAliveService(1L);
  }

  @Test
  public void shouldWrap(){
    System.out.println(service);

    final var flux = Flux.interval(Duration.ofMillis(700)).take(3)
        .map(aLong -> String.format("event%d", aLong));

    final var keepAlive = service.wrap(flux).take(6);

    StepVerifier
        .create(keepAlive)
        .expectNextMatches(sse -> sse.data().equals("event0"))
        .expectNextMatches(sse -> sse.comment().equals("keep alive"))
        .expectNextMatches(sse -> sse.data().equals("event1"))
        .expectNextMatches(sse -> sse.comment().equals("keep alive"))
        .expectNextMatches(sse -> sse.data().equals("event2"))
        .expectNextMatches(sse -> sse.comment().equals("keep alive"))
        .expectComplete()
        .verify();
  }
}
