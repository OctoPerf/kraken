package com.kraken.tools.webclient;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface Client {

  int NUM_RETRIES = 5;
  Duration FIRST_BACKOFF = Duration.ofMillis(100);

  default <T> Mono<T> retry(final Mono<T> mono, final Logger log) {
    return mono.retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(throwable -> throwable.getCause() instanceof WebClientResponseException, throwable -> {
          log.info(((WebClientResponseException) throwable.getCause()).getResponseBodyAsString());
        });
  }

  default <T> Flux<T> retry(final Flux<T> flux, final Logger log) {
    return flux.retryBackoff(NUM_RETRIES, FIRST_BACKOFF)
        .doOnError(throwable -> throwable.getCause() instanceof WebClientResponseException, throwable -> {
          log.info(((WebClientResponseException) throwable.getCause()).getResponseBodyAsString());
        });
  }

  default String basicAuthorizationHeader(final String login, final String password) {
    final var credentials = login + ":" + password;
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(UTF_8));
    return "Basic " + encoded;
  }
}
