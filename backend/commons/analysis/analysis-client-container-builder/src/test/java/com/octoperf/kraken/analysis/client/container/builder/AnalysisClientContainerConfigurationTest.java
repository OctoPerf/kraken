package com.octoperf.kraken.analysis.client.container.builder;

import com.octoperf.kraken.analysis.client.api.AnalysisClient;
import com.octoperf.kraken.analysis.client.api.AnalysisClientBuilder;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AnalysisClientContainerConfigurationTest {

  @Mock
  ContainerProperties properties;
  @Mock
  AnalysisClientBuilder clientBuilder;
  @Mock
  AnalysisClient client;

  AnalysisClientContainerConfiguration configuration;

  @BeforeEach
  public void setUp() {
    given(properties.getApplicationId()).willReturn("applicationId");
    given(properties.getProjectId()).willReturn("projectId");
    given(clientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .mode(AuthenticationMode.CONTAINER)
        .applicationId(properties.getApplicationId())
        .projectId(properties.getProjectId())
        .build())).willReturn(Mono.just(client));
    configuration = new AnalysisClientContainerConfiguration();
  }

  @Test
  public void shouldCreateClient() {
    Assertions.assertThat(configuration.analysisClient(clientBuilder, properties)).isNotNull();
  }
}