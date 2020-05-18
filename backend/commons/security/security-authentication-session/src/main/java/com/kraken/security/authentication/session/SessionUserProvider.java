package com.kraken.security.authentication.session;


import com.kraken.security.authentication.api.UserProvider;
import com.kraken.security.entity.token.KrakenTokenUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SessionUserProvider implements UserProvider {

  @NonNull Mono<SecurityContext> securityContextMono;

  @Override
  public Mono<KrakenTokenUser> getAuthenticatedUser() {
    return securityContextMono
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getDetails)
        .map(KrakenTokenUser.class::cast);
  }

  @Override
  public Mono<String> getTokenValue() {
    return securityContextMono
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .map(Jwt.class::cast)
        .map(Jwt::getTokenValue);
  }

}
