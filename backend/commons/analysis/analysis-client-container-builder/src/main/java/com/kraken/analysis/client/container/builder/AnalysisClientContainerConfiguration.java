package com.kraken.analysis.client.container.builder;

import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.client.api.AnalysisClientBuilder;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnalysisClientContainerConfiguration {
  @Bean
  public AnalysisClient analysisClient(@NonNull final AnalysisClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.mode(AuthenticationMode.CONTAINER).applicationId(properties.getApplicationId()).build();
  }
}
