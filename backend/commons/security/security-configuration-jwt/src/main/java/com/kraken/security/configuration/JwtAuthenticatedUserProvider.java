package com.kraken.security.confioguration;


import com.kraken.security.configuration.AuthenticatedUserProvider;
import com.kraken.security.configuration.entity.KrakenUser;
import lombok.extern.slf4j.Slf4j;
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
        .map(authentication -> {
          log.info("" + authentication);
          log.info("" + authentication.getAuthorities());
          log.info("" + authentication.getCredentials());
          log.info("" + authentication.getDetails());
          log.info("" + authentication.getPrincipal());
//          TODO return Kraken User
          return null;
        });
  }

}
