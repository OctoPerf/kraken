package com.octoperf.kraken.runtime.client.web;

import com.octoperf.kraken.runtime.client.api.RuntimeWatchClient;
import com.octoperf.kraken.runtime.entity.log.Log;
import com.octoperf.kraken.runtime.entity.task.Task;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
@FieldDefaults(level = PROTECTED, makeFinal = true)
class WebRuntimeWatchClient implements RuntimeWatchClient {

  WebClient webClient;

  WebRuntimeWatchClient(@NonNull final WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Flux<Log> watchLogs() {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/logs/watch").build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(Log.class)
        .doOnSubscribe(subscription -> log.info("Watch logs"))
        .doOnError(t -> log.error("Failed to watch logs", t));
  }

  @Override
  public Flux<List<Task>> watchTasks() {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/task/watch").build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<List<Task>>() {
        })
        .doOnSubscribe(subscription -> log.info("Watch tasks"))
        .doOnError(t -> log.error("Failed to watch tasks", t));
  }

}
