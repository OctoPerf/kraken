package com.kraken.runtime.server.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static com.kraken.security.entity.token.KrakenRole.*;

@EnableWebFluxSecurity
public class RuntimeSecurityConfiguration {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http, final Converter<Jwt, Mono<AbstractAuthenticationToken>> converter) {
    http
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/container/**").hasAnyAuthority(USER.name())
        .pathMatchers("/host/list").hasAnyAuthority(USER.name())
        .pathMatchers("/host/**").hasAnyAuthority(ADMIN.name())
        .pathMatchers("/logs/**").hasAnyAuthority(USER.name())
        .pathMatchers("/task/events").hasAnyAuthority(API.name())
        .pathMatchers("/task/**").hasAnyAuthority(USER.name())
        .anyExchange().denyAll()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(converter);
    return http.build();
  }

}
