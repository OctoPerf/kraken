package com.kraken.security.authentication.session;


import com.kraken.security.entity.token.KrakenTokenUserTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SessionUserProviderTest {

  @Mock
  Authentication authentication;
  @Mock
  SecurityContext securityContext;
  @Mock
  Jwt jwt;

  SessionUserProvider provider;

  @Before
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