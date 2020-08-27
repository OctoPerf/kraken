package com.octoperf.kraken.storage.client.container.builder;

import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class StorageClientContainerConfiguration {
  @Bean
  public Mono<StorageClient> storageClient(@NonNull final StorageClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.build(
        AuthenticatedClientBuildOrder.builder()
            .applicationId(properties.getApplicationId())
            .projectId(properties.getProjectId())
            .mode(AuthenticationMode.CONTAINER)
            .build()
    );
  }
}
