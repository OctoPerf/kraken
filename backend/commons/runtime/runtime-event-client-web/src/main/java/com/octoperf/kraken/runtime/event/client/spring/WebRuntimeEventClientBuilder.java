package com.octoperf.kraken.runtime.event.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.runtime.event.client.api.RuntimeEventClient;
import com.octoperf.kraken.runtime.event.client.api.RuntimeEventClientBuilder;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.WebAuthenticatedClientBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebRuntimeEventClientBuilder extends WebAuthenticatedClientBuilder<RuntimeEventClient, BackendClientProperties> implements RuntimeEventClientBuilder {

  @NonNull ObjectMapper mapper;

  public WebRuntimeEventClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                      final BackendClientProperties properties,
                                      @NonNull final ObjectMapper mapper) {
    super(exchangeFilterFactories, properties);
    this.mapper = mapper;
  }

  @Override
  public Mono<RuntimeEventClient> build(final AuthenticatedClientBuildOrder order) {
    return Mono.just(new WebRuntimeEventClient(getWebClientBuilder(order).build(), mapper));
  }

}
