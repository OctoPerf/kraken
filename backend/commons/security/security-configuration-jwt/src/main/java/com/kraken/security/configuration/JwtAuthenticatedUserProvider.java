package com.kraken.security.configuration;


import com.kraken.security.configuration.entity.KrakenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
class JwtAuthenticatedUserProvider implements AuthenticatedUserProvider {

  public Mono<KrakenUser> getAuthenticatedUser() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getDetails)
        .map(KrakenUser.class::cast);
  }

}
