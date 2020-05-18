package com.kraken.tools.reactor.utils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class ReactorUtils {

  public static <T, U> void waitFor(final Flux<T> flux, final Mono<U> mono, final Duration delay) {
    try {
      final var blocker = new CountDownLatch(1);
      flux
          .subscribeOn(Schedulers.elastic())
          .takeUntilOther(mono
              .delayElement(delay)
              .doFinally(signalType -> blocker.countDown()))
          .blockLast();
      blocker.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
