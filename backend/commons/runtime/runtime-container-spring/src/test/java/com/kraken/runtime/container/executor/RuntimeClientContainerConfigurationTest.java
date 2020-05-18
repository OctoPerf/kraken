package com.kraken.runtime.container.executor;

import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.runtime.client.api.RuntimeClient;
import com.kraken.runtime.client.api.RuntimeClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeClientContainerConfigurationTest {

  @Mock
  ContainerProperties properties;
  @Mock
  RuntimeClientBuilder clientBuilder;
  @Mock
  RuntimeClient client;

  RuntimeClientContainerConfiguration configuration;

  @Before
  public void setUp() {
    given(properties.getApplicationId()).willReturn("applicationId");
    given(clientBuilder.mode(AuthenticationMode.CONTAINER)).willReturn(clientBuilder);
    given(clientBuilder.applicationId("applicationId")).willReturn(clientBuilder);
    given(clientBuilder.build()).willReturn(client);
    configuration = new RuntimeClientContainerConfiguration();
  }

  @Test
  public void shouldCreateClient() {
    Assertions.assertThat(configuration.runtimeClient(clientBuilder, properties)).isNotNull();
  }
}