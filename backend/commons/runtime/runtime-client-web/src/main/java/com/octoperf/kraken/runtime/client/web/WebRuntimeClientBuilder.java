package com.octoperf.kraken.runtime.client.web;

import com.octoperf.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.client.api.RuntimeClientBuilder;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.spring.WebAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebRuntimeClientBuilder extends WebAuthenticatedClientBuilder<RuntimeClient, RuntimeClientProperties> implements RuntimeClientBuilder {

  public WebRuntimeClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                 final RuntimeClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public Mono<RuntimeClient> build() {
    return Mono.just(new WebRuntimeClient(webClientBuilder.build()));
  }

}
