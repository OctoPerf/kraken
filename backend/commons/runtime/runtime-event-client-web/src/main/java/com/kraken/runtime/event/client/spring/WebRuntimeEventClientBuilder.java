package com.kraken.runtime.event.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kraken.config.runtime.client.api.RuntimeClientProperties;
import com.kraken.runtime.event.client.api.RuntimeEventClient;
import com.kraken.runtime.event.client.api.RuntimeEventClientBuilder;
import com.kraken.security.authentication.api.ExchangeFilterFactory;
import com.kraken.security.authentication.client.spring.AbstractAuthenticatedClientBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebRuntimeEventClientBuilder extends AbstractAuthenticatedClientBuilder<RuntimeEventClient, RuntimeClientProperties> implements RuntimeEventClientBuilder {

  @NonNull ObjectMapper mapper;

  public WebRuntimeEventClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                      final RuntimeClientProperties properties,
                                      final ObjectMapper mapper) {
    super(exchangeFilterFactories, properties);
    this.mapper = requireNonNull(mapper);
  }

  @Override
  public RuntimeEventClient build() {
    return new WebRuntimeEventClient(webClientBuilder.build(), mapper);
  }

}
