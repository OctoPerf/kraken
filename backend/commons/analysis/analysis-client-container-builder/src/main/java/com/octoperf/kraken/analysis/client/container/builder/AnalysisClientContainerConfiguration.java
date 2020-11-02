package com.octoperf.kraken.analysis.client.container.builder;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
class AnalysisClientContainerConfiguration {
  @Bean
  public Mono<AnalysisClient> analysisClient(@NonNull final AnalysisClientBuilder clientBuilder, @NonNull final ContainerProperties properties) {
    return clientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .container(properties.getApplicationId(), properties.getProjectId())
        .build());
  }
}
