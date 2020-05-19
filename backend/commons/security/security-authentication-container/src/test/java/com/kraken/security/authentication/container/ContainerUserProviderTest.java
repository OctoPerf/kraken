package com.kraken.security.authentication.container;

import com.kraken.config.security.client.api.SecurityClientCredentialsProperties;
import com.kraken.config.security.client.api.SecurityClientProperties;
import com.kraken.config.security.container.api.SecurityContainerProperties;
import com.kraken.security.client.api.SecurityClient;
import com.kraken.security.decoder.api.TokenDecoder;
import com.kraken.security.entity.token.KrakenTokenTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContainerUserProviderTest {
  @Mock(lenient = true)
  SecurityClientProperties clientProperties;
  @Mock
  SecurityClientCredentialsProperties credentialsProperties;
  @Mock(lenient = true)
  SecurityContainerProperties containerProperties;
  @Mock
  TokenDecoder decoder;
  @Mock
  SecurityClient client;
  @Mock
  Function<Long, Mono<String>> refresh;

  ContainerUserProvider userProvider;

  @BeforeEach
  public void setUp() {
    given(containerProperties.getAccessToken()).willReturn("accessToken");
    given(containerProperties.getRefreshToken()).willReturn("refreshToken");
    given(containerProperties.getRefreshExpiresIn()).willReturn(1800L);
    given(containerProperties.getRefreshMinValidity()).willReturn(300L);
    given(clientProperties.getContainer()).willReturn(credentialsProperties);
    userProvider = new ContainerUserProvider(clientProperties, containerProperties, decoder, client);
  }

  @Test
  public void shouldCreateToken() {
    given(client.refreshToken(credentialsProperties, "refreshToken")).willReturn(Mono.just(KrakenTokenTest.KRAKEN_TOKEN));
    final var token = userProvider.newToken().block();
    assertThat(token).isNotNull();
    assertThat(token).isEqualTo(KrakenTokenTest.KRAKEN_TOKEN);
  }

  @Test
  public void shouldRefreshToken() {
    given(client.refreshToken(credentialsProperties, "refreshToken")).willReturn(Mono.just(KrakenTokenTest.KRAKEN_TOKEN));
    final var tokenRefresh = userProvider.refreshToken(KrakenTokenTest.KRAKEN_TOKEN).block();
    assertThat(tokenRefresh).isNotNull();
    assertThat(tokenRefresh).isEqualTo(KrakenTokenTest.KRAKEN_TOKEN);
  }

  @Test
  public void shouldPeriodicRefresh() {
    given(refresh.apply(any())).willAnswer(invocation -> Mono.just(invocation.getArgument(0).toString()));

    StepVerifier
        .withVirtualTime(() -> userProvider.periodicRefresh(refresh).take(3))
        .expectSubscription()
        .expectNext("0")
        .expectNoEvent(Duration.ofSeconds(1500))
        .expectNext("1")
        .expectNoEvent(Duration.ofSeconds(1500))
        .expectNext("2")
        .expectComplete()
        .verify(Duration.ofSeconds(3000));

    verify(refresh).apply(0L);
    verify(refresh).apply(1L);
    verify(refresh).apply(2L);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(ContainerUserProvider.class);
  }
}