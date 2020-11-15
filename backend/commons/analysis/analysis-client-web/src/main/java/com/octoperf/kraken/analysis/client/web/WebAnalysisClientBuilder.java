package com.octoperf.kraken.analysis.client.web;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.WebAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebAnalysisClientBuilder extends WebAuthenticatedClientBuilder<AnalysisClient, BackendClientProperties> implements AnalysisClientBuilder {

  public WebAnalysisClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                  final BackendClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public Mono<AnalysisClient> build(final AuthenticatedClientBuildOrder order) {
    return Mono.just(new WebAnalysisClient(getWebClientBuilder(order).build()));
  }
}
