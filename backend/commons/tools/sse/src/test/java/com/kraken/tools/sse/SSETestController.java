package com.kraken.tools.sse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController()
@RequestMapping("/test")
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class SSETestController {

  @NonNull
  SSEService sse;

  @GetMapping(value = "/watch")
  public Flux<ServerSentEvent<String>> watch() {
    return this.sse.keepAlive(Flux.interval(Duration.ofMillis(700)).map(aLong -> String.format("event%d", aLong))).take(6);
  }

  @GetMapping(value = "/merge")
  public Flux<SSEWrapper> merge() {
    final var strFlux = Flux.interval(Duration.ofMillis(500)).take(3)
        .map(aLong -> String.format("event%d", aLong));
    final var longFlux = Flux.interval(Duration.ofMillis(700)).take(3);
    return this.sse.<Object>merge("String", strFlux, "Long", longFlux ).take(6);
  }
}
