package com.kraken.runtime.container.executor;

import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.client.api.RuntimeClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RuntimeClientContainerConfiguration {

  @Bean
  public RuntimeClient runtimeClient(@NonNull final RuntimeClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(properties.getApplicationId()).build();
  }
}
