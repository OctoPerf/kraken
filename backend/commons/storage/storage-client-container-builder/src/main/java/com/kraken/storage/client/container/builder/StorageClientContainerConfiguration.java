package com.kraken.storage.client.container.builder;

import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StorageClientContainerConfiguration {
  @Bean
  public StorageClient storageClient(@NonNull final StorageClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(properties.getApplicationId()).build();
  }
}
