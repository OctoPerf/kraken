package com.octoperf.kraken.security.authentication.service.account;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import com.octoperf.kraken.security.client.api.SecurityClient;
import com.octoperf.kraken.security.client.api.SecurityClientBuilder;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.SERVICE_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ServiceAccountUserProviderFactoryTest {

  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  TokenDecoder decoder;
  @Mock(lenient = true)
  SecurityClientBuilder clientBuilder;
  @Mock
  SecurityClient client;

  ServiceAccountUserProviderFactory factory;

  @BeforeEach
  public void setUp() {
    given(clientBuilder.build()).willReturn(Mono.just(client));
    factory = new ServiceAccountUserProviderFactory(clientProperties, decoder, clientBuilder);
  }

  @Test
  public void shouldCreate() {
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isNotNull();
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(SERVICE_ACCOUNT);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ServiceAccountUserProviderFactory.class);
  }

}