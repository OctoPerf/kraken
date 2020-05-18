package com.kraken.analysis.client.container.builder;

import com.kraken.analysis.client.api.AnalysisClient;
import com.kraken.analysis.client.api.AnalysisClientBuilder;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AnalysisClientContainerConfigurationTest {

  @Mock
  ContainerProperties properties;
  @Mock
  AnalysisClientBuilder clientBuilder;
  @Mock
  AnalysisClient client;

  AnalysisClientContainerConfiguration configuration;

  @Before
  public void setUp() {
    given(properties.getApplicationId()).willReturn("applicationId");
    given(clientBuilder.mode(AuthenticationMode.CONTAINER)).willReturn(clientBuilder);
    given(clientBuilder.applicationId("applicationId")).willReturn(clientBuilder);
    given(clientBuilder.build()).willReturn(client);
    configuration = new AnalysisClientContainerConfiguration();
  }

  @Test
  public void shouldCreateClient() {
    Assertions.assertThat(configuration.analysisClient(clientBuilder, properties)).isNotNull();
  }
}