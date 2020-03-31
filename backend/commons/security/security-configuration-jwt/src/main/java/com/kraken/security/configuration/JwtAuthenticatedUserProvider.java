package com.kraken.security.configuration;


import com.kraken.security.configuration.entity.KrakenUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JwtAuthenticatedUserProvider implements AuthenticatedUserProvider {

  @NonNull Mono<SecurityContext> securityContextMono;

  @Override
  public Mono<KrakenUser> getAuthenticatedUser() {
    return securityContextMono
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getDetails)
        .map(KrakenUser.class::cast);
  }

  @Override
  public Mono<String> getAuthorizationHeader() {
    return securityContextMono
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getPrincipal)
        .map(Jwt.class::cast)
        .map(Jwt::getTokenValue);
  }

}
