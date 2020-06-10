package com.octoperf.kraken.runtime.container.executor;

import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.client.api.RuntimeClient;
import com.octoperf.kraken.runtime.client.api.RuntimeClientBuilder;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class RuntimeClientContainerConfiguration {

  @Bean
  public Mono<RuntimeClient> runtimeClient(@NonNull final RuntimeClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(properties.getApplicationId()).build();
  }
}
