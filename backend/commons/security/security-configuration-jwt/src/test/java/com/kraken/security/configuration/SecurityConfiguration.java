package com.kraken.security.configuration;

// https://blog.ineat-group.com/2019/01/securiser-vos-apis-spring-webflux-avec-keycloak-et-oauth2/

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class SecurityConfiguration {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http, final Converter<Jwt, Mono<AbstractAuthenticationToken>> converter) {
    http
        .authorizeExchange()
        .pathMatchers("/static/**")
        .permitAll()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
//        .pathMatchers("/files/**").hasAuthority("USER")
//        .pathMatchers("/files/**").hasAuthority("ADMIN")
        .pathMatchers("/test/user").hasAuthority("USER")
        .pathMatchers("/test/admin").hasAuthority("ADMIN")
        .anyExchange().denyAll()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(converter);
    return http.build();
  }

}
