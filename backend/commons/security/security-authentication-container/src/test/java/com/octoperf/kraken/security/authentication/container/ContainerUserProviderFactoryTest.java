package com.octoperf.kraken.security.authentication.container;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.config.security.container.api.SecurityContainerProperties;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.client.api.SecurityClientBuilder;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.CONTAINER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ContainerUserProviderFactoryTest {

  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  SecurityContainerProperties containerProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClientBuilder clientBuilder;
  @Mock
  SecurityClient client;

  ContainerUserProviderFactory factory;

  @BeforeEach
  public void setUp() throws IOException{
    factory = new ContainerUserProviderFactory(clientProperties, containerProperties, decoder, clientBuilder);
  }

  @Test
  public void shouldCreate() {
    given(clientBuilder.build()).willReturn(Mono.just(client));
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isNotNull();
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(CONTAINER);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ContainerUserProviderFactory.class);
  }

}