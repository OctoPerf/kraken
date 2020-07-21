package com.octoperf.kraken.security.authentication.session;


import com.octoperf.kraken.security.entity.token.KrakenTokenUserTest;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SessionUserProviderTest {

  @Mock
  Authentication authentication;
  @Mock(lenient = true)
  SecurityContext securityContext;
  @Mock
  Jwt jwt;

  SessionUserProvider provider;

  @BeforeEach
  public void setUp() {
    given(securityContext.getAuthentication()).willReturn(authentication);
    provider = new SessionUserProvider(Mono.just(securityContext));
  }

  @Test
  public void shouldGetAuthenticatedUser() {
    given(authentication.getDetails()).willReturn(KrakenTokenUserTest.KRAKEN_USER);
    final var user = provider.getAuthenticatedUser().block();
    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(KrakenTokenUserTest.KRAKEN_USER);
  }

  @Test
  public void shouldGetTokenValue() {
    given(authentication.getPrincipal()).willReturn(jwt);
    given(jwt.getTokenValue()).willReturn("token");
    final var user = provider.getTokenValue().block();
    assertThat(user).isNotNull();
    assertThat(user).isEqualTo("token");
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(SessionUserProvider.class);
  }
}