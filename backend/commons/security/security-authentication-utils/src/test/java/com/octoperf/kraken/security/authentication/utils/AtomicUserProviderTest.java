package com.octoperf.kraken.security.authentication.utils;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.decoder.api.TokenDecoder;
import com.octoperf.kraken.security.entity.token.KrakenTokenTest;
import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.security.entity.token.KrakenToken;
import com.octoperf.kraken.security.entity.token.KrakenTokenUser;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AtomicUserProviderTest {

  private static class TestAtomicUserProvider extends AtomicUserProvider {

    public KrakenToken newToken;
    public KrakenToken refreshToken;

    public TestAtomicUserProvider(TokenDecoder decoder, Long minValidity) {
      super(decoder, minValidity);
    }

    @Override
    protected Mono<KrakenToken> newToken() {
      return Mono.just(newToken);
    }

    @Override
    protected Mono<KrakenToken> refreshToken(KrakenToken token) {
      return Mono.just(refreshToken);
    }
  }

  @Mock
  TokenDecoder decoder;

  TestAtomicUserProvider userProvider;

  @BeforeEach
  public void setUp() {
    userProvider = new TestAtomicUserProvider(decoder, 60L);
  }

  @Test
  public void shouldGetTokenValue() throws IOException {
    // First login
    given(decoder.decode("accessToken")).willReturn(KrakenTokenUserTest.KRAKEN_USER);
    userProvider.newToken = KrakenTokenTest.KRAKEN_TOKEN;
    final var token = userProvider.getTokenValue().block();
    assertThat(token).isNotNull();
    assertThat(token).isEqualTo("accessToken");

    // Then refresh
    userProvider.refreshToken = KrakenTokenTest.KRAKEN_TOKEN;
    final var tokenRefresh = userProvider.getTokenValue().block();
    assertThat(tokenRefresh).isNotNull();
    assertThat(tokenRefresh).isEqualTo("accessToken");

    // Then same
    final var decoded = KrakenTokenUser.builder()
        .issuedAt(Instant.now().plusSeconds(30))
        .expirationTime(Instant.now().plusSeconds(1800))
        .userId("userId")
        .username("username")
        .email("email")
        .roles(ImmutableList.of(KrakenRole.USER))
        .groups(ImmutableList.of("/default-kraken"))
        .currentGroup("/default-kraken")
        .build();
    given(decoder.decode("accessToken")).willReturn(decoded);
    final var tokenSame = userProvider.getTokenValue().block();
    assertThat(tokenSame).isNotNull();
    assertThat(tokenSame).isSameAs(tokenRefresh);

    // Finally user
    final var user = userProvider.getAuthenticatedUser().block();
    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(decoded);
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(TestAtomicUserProvider.class);
  }
}