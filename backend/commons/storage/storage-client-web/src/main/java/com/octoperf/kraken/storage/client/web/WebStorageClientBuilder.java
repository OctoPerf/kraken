package com.octoperf.kraken.storage.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.security.authentication.api.ExchangeFilterFactory;
import com.octoperf.kraken.security.authentication.client.spring.WebAuthenticatedClientBuilder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class WebStorageClientBuilder extends WebAuthenticatedClientBuilder<StorageClient, BackendClientProperties> implements StorageClientBuilder {

  ObjectMapper mapper;
  ObjectMapper yamlMapper;

  public WebStorageClientBuilder(final List<ExchangeFilterFactory> exchangeFilterFactories,
                                 final BackendClientProperties properties,
                                 @NonNull final ObjectMapper mapper,
                                 @NonNull @Qualifier("yamlObjectMapper") final ObjectMapper yamlMapper) {
    super(exchangeFilterFactories, properties);
    this.mapper = mapper;
    this.yamlMapper = yamlMapper;
  }

  @Override
  public Mono<StorageClient> build() {
    return Mono.just(new WebStorageClient(webClientBuilder.build(), mapper, yamlMapper));
  }

}
