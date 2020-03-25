package com.kraken.storage.configuration;

// https://blog.ineat-group.com/2019/01/securiser-vos-apis-spring-webflux-avec-keycloak-et-oauth2/

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
    http
        .authorizeExchange()
        .anyExchange().authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();
    return http.build();

  }
}
