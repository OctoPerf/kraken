package com.kraken.commons.command.client;

import com.kraken.commons.command.entity.Command;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class CommandWebClient implements CommandClient {

  WebClient webClient;

  CommandWebClient(@Qualifier("webClientCommand") final WebClient webClient) {
    this.webClient = requireNonNull(webClient);
  }

  public Mono<String> execute(final Command command) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/execute").build())
        .body(BodyInserters.fromObject(command))
        .retrieve()
        .bodyToMono(String.class);
  }
}
