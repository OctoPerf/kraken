package com.octoperf.kraken.security.authentication.impersonate;

import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
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

import static com.octoperf.kraken.security.authentication.api.AuthenticationMode.IMPERSONATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ImpersonateUserProviderFactoryTest {

  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClientBuilder clientBuilder;
  @Mock
  SecurityClient client;

  ImpersonateUserProviderFactory factory;

  @BeforeEach
  public void setUp() {
    factory = new ImpersonateUserProviderFactory(clientProperties, decoder, clientBuilder);
  }

  @Test
  public void shouldCreate() {
    given(clientBuilder.build()).willReturn(Mono.just(client));
    final var userProvider = factory.create("userId");
    assertThat(userProvider).isNotNull();
  }

  @Test
  public void shouldGetMode() {
    assertThat(factory.getMode()).isEqualTo(IMPERSONATE);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ImpersonateUserProviderFactory.class);
  }

}