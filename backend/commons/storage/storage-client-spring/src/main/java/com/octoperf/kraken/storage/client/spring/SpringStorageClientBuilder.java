package com.octoperf.kraken.storage.client.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octoperf.kraken.security.authentication.api.UserProviderFactory;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.security.authentication.client.spring.SpringAuthenticatedClientBuilder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import com.octoperf.kraken.storage.file.StorageService;
import com.octoperf.kraken.storage.file.StorageServiceBuilder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import reactor.core.publisher.Mono;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringStorageClientBuilder extends SpringAuthenticatedClientBuilder<StorageClient> implements StorageClientBuilder {

  StorageServiceBuilder storageServiceBuilder;
  ObjectMapper mapper;
  ObjectMapper yamlMapper;

  public SpringStorageClientBuilder(final List<UserProviderFactory> userProviderFactories,
                                    @NonNull final StorageServiceBuilder storageServiceBuilder,
                                    @NonNull final ObjectMapper mapper,
                                    @NonNull @Qualifier("yamlObjectMapper") final ObjectMapper yamlMapper) {
    super(userProviderFactories);
    this.storageServiceBuilder = storageServiceBuilder;
    this.mapper = mapper;
    this.yamlMapper = yamlMapper;
  }

  @Override
  public Mono<StorageClient> build(final AuthenticatedClientBuildOrder order) {
    return super.getOwner(order).map(owner -> new SpringStorageClient(storageServiceBuilder.build(owner), mapper, yamlMapper));
  }

}
