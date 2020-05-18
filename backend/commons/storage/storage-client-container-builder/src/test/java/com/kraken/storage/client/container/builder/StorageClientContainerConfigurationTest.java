package com.kraken.storage.client.container.builder;

import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.security.authentication.api.AuthenticationMode;
import com.kraken.storage.client.api.StorageClient;
import com.kraken.storage.client.api.StorageClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StorageClientContainerConfigurationTest {

  @Mock
  ContainerProperties properties;
  @Mock
  StorageClientBuilder clientBuilder;
  @Mock
  StorageClient client;

  StorageClientContainerConfiguration configuration;

  @Before
  public void setUp() {
    given(properties.getApplicationId()).willReturn("applicationId");
    given(clientBuilder.mode(AuthenticationMode.CONTAINER)).willReturn(clientBuilder);
    given(clientBuilder.applicationId("applicationId")).willReturn(clientBuilder);
    given(clientBuilder.build()).willReturn(client);
    configuration = new StorageClientContainerConfiguration();
  }

  @Test
  public void shouldCreateClient() {
    Assertions.assertThat(configuration.storageClient(clientBuilder, properties)).isNotNull();
  }
}