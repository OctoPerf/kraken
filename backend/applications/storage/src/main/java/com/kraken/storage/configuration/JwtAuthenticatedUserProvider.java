package com.kraken.storage.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
class JwtAuthenticatedUserProvider implements AuthenticatedUserProvider {

  public Mono<String> getAuthenticatedUser() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .map(authentication -> {
          log.info("" + authentication);
          log.info("" + authentication.getAuthorities());
          log.info("" + authentication.getCredentials());
          log.info("" + authentication.getDetails());
          log.info("" + authentication.getPrincipal());
          return authentication.getName();
        });
  }

}
