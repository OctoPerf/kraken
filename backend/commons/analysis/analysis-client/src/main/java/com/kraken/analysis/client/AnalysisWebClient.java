package com.kraken.analysis.client;

import com.kraken.analysis.entity.DebugEntry;
import com.kraken.analysis.entity.Result;
import com.kraken.analysis.entity.ResultStatus;
import com.kraken.storage.entity.StorageNode;
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
class AnalysisWebClient implements AnalysisClient {

  WebClient webClient;
  AnalysisClientProperties properties;

  AnalysisWebClient(final AnalysisClientProperties properties, @Qualifier("webClientAnalysis") final WebClient webClient) {
    this.webClient = requireNonNull(webClient);
    this.properties = requireNonNull(properties);
  }

  @Override
  public Mono<StorageNode> create(final Result result) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .build())
        .body(BodyInserters.fromObject(result))
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  @Override
  public Mono<String> delete(final String resultId) {
    return webClient.delete()
        .uri(uriBuilder -> uriBuilder.path("/result")
            .queryParam("resultId", resultId)
            .build())
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Mono<StorageNode> setStatus(final String resultId, final ResultStatus status) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/status")
            .pathSegment(status.toString())
            .queryParam("resultId", resultId)
            .build())
        .retrieve()
        .bodyToMono(StorageNode.class);
  }

  @Override
  public Mono<DebugEntry> debug(final DebugEntry debug) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/result/debug")
            .build())
        .body(BodyInserters.fromObject(debug))
        .retrieve()
        .bodyToMono(DebugEntry.class);
  }
}
