package com.octoperf.kraken.storage.client.container.builder;

import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.authentication.client.api.AuthenticatedClientBuildOrder;
import com.octoperf.kraken.storage.client.api.StorageClient;
import com.octoperf.kraken.storage.client.api.StorageClientBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StorageClientContainerConfigurationTest {

  @Mock
  ContainerProperties properties;
  @Mock
  StorageClientBuilder clientBuilder;
  @Mock
  StorageClient client;

  StorageClientContainerConfiguration configuration;

  @BeforeEach
  public void setUp() {
    given(properties.getApplicationId()).willReturn("applicationId");
    given(properties.getProjectId()).willReturn("projectId");
    given(clientBuilder.build(AuthenticatedClientBuildOrder.builder()
        .applicationId(properties.getApplicationId())
        .projectId(properties.getProjectId())
        .mode(AuthenticationMode.CONTAINER)
        .build())).willReturn(Mono.just(client));
    configuration = new StorageClientContainerConfiguration();
  }

  @Test
  public void shouldCreateClient() {
    Assertions.assertThat(configuration.storageClient(clientBuilder, properties)).isNotNull();
  }
}