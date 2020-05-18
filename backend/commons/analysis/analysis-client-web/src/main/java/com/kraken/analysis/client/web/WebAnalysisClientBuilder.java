package com.kraken.analysis.client.web;

import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.client.api.AnalysisClientBuilder;
import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.spring.AbstractAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebAnalysisClientBuilder extends AbstractAuthenticatedClientBuilder<AnalysisClient, AnalysisClientProperties> implements AnalysisClientBuilder {

  public WebAnalysisClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                  final AnalysisClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public AnalysisClient build() {
    return new WebAnalysisClient(webClientBuilder.build());
  }
}
