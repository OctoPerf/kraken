package com.kraken.tools.reactor.utils;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactorUtilsTest {

  @Test
  public void shouldWaitFor() throws InterruptedException {
    final var logsBuilder = ImmutableList.<String>builder();
    final var flux = Flux.interval(Duration.ofMillis(100)).map(aLong -> "=> " + aLong).doOnNext(logsBuilder::add);
    final var wait = Mono.just("Done");
    ReactorUtils.waitFor(flux, wait, Duration.ofSeconds(2));
    final var logs = logsBuilder.build();
    Assertions.assertThat(logs.size()).isLessThan(20);
  }
}
