package com.kraken.security.authentication.impersonate;

import com.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenTokenTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ImpersonateUserProviderTest {
  @Mock
  SecurityClientProperties clientProperties;
  @Mock
  SecurityClientCredentialsProperties credentialsProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClient client;

  ImpersonateUserProvider userProvider;

  @Before
  public void setUp() {
    given(clientProperties.getApi()).willReturn(credentialsProperties);
    userProvider = new ImpersonateUserProvider(clientProperties, decoder, client, "userId");
  }

  @Test
  public void shouldCreateToken() throws IOException {
    given(client.impersonate(credentialsProperties, "userId")).willReturn(Mono.just(KrakenTokenTest.KRAKEN_TOKEN));
    final var token = userProvider.newToken().block();
    assertThat(token).isNotNull();
    assertThat(token).isEqualTo(KrakenTokenTest.KRAKEN_TOKEN);
  }

  @Test
  public void shouldRefreshToken() throws IOException {
    given(client.refreshToken(credentialsProperties, "refreshToken")).willReturn(Mono.just(KrakenTokenTest.KRAKEN_TOKEN));
    final var tokenRefresh = userProvider.refreshToken(KrakenTokenTest.KRAKEN_TOKEN).block();
    assertThat(tokenRefresh).isNotNull();
    assertThat(tokenRefresh).isEqualTo(KrakenTokenTest.KRAKEN_TOKEN);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ImpersonateUserProvider.class);
  }
}