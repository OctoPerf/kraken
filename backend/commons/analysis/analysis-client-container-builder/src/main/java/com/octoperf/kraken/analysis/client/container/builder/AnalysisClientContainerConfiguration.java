package com.octoperf.kraken.analysis.client.container.builder;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class AnalysisClientContainerConfiguration {
  @Bean
  public Mono<AnalysisClient> analysisClient(@NonNull final AnalysisClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(properties.getApplicationId()).build();
  }
}
