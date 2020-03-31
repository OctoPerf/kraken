package com.kraken.security.configuration;


import com.kraken.security.configuration.entity.KrakenUserTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticatedUserProviderTest {

  @Mock
  Authentication authentication;

  @Mock
  SecurityContext securityContext;

  @Test
  public void shouldGetAuthenticatedUser() {
    given(securityContext.getAuthentication()).willReturn(authentication);
    given(authentication.getDetails()).willReturn(KrakenUserTest.KRAKEN_USER);
    final var provider = new JwtAuthenticatedUserProvider(Mono.just(securityContext));
    final var user = provider.getAuthenticatedUser().block();
    assertThat(user).isNotNull();
    assertThat(user).isEqualTo(KrakenUserTest.KRAKEN_USER);
  }
}