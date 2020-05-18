package com.kraken.runtime.client.web;

import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.client.api.RuntimeClientBuilder;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.spring.AbstractAuthenticatedClientBuilder;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebRuntimeClientBuilder extends AbstractAuthenticatedClientBuilder<RuntimeClient, RuntimeClientProperties> implements RuntimeClientBuilder {

  public WebRuntimeClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                 final RuntimeClientProperties properties) {
    super(exchangeFilterFactories, properties);
  }

  @Override
  public RuntimeClient build() {
    return new WebRuntimeClient(webClientBuilder.build());
  }

}
