package com.kraken.runtime.event.client.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.kraken.runtime.event.*;
import com.kraken.runtime.event.client.api.RuntimeEventClient;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebRuntimeEventClient implements RuntimeEventClient {

  WebClient webClient;
  ObjectMapper mapper;
  Map<String, Class<? extends TaskEvent>> eventClasses;

  WebRuntimeEventClient(final WebClient webClient,
                        final ObjectMapper mapper) {
    this.webClient = requireNonNull(webClient);
    this.mapper = requireNonNull(mapper);

    this.eventClasses = ImmutableList.of(
        TaskCreatedEvent.class,
        TaskExecutedEvent.class,
        TaskStatusUpdatedEvent.class,
        TaskCancelledEvent.class,
        TaskRemovedEvent.class)
        .stream()
        .collect(Collectors.toUnmodifiableMap(Class::getSimpleName, aClass -> aClass));
  }

  @Override
  public Flux<TaskEvent> events() {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/task/events").build())
        .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
        .retrieve()
        .bodyToFlux(String.class)
        .doOnSubscribe(subscription -> log.info("Watch task events"))
        .doOnError(t -> log.error("Failed to watch task events", t))
        .flatMap(sseWrapper -> Mono.fromCallable(() -> {
          final JsonNode node = mapper.readTree(sseWrapper);
          final var type = node.get("type").asText();
          final var clazz = eventClasses.get(type);
          return mapper.readValue(node.get("value").toString(), clazz);
        }));
  }
}
