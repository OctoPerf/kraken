package com.octoperf.kraken.security.configuration;

import com.octoperf.kraken.security.entity.token.KrakenRole;
import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import static com.octoperf.kraken.security.entity.token.KrakenRole.*;

@ExcludeFromObfuscation
@EnableWebFluxSecurity
public class SecurityConfiguration {
  @Bean
  SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http,
                                                   final Converter<Jwt, Mono<AbstractAuthenticationToken>> converter,
                                                   final KrakenServerAuthenticationConverter tokenConverter) {
    http
        .cors().and()
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        // Analysis
        .pathMatchers("/result/**").hasAnyAuthority(USER.name())
        .pathMatchers("/user-events/**").hasAnyAuthority(API.name())
        // Runtime
        .pathMatchers("/container/**").hasAnyAuthority(USER.name())
        .pathMatchers("/host/list").hasAnyAuthority(USER.name())
        .pathMatchers("/host/**").hasAnyAuthority(ADMIN.name())
        .pathMatchers("/logs/**").hasAnyAuthority(USER.name())
        .pathMatchers("/task/events").hasAnyAuthority(API.name())
        .pathMatchers("/task/**").hasAnyAuthority(USER.name())
        // SSE
        .pathMatchers("/sse/watch").hasAnyAuthority(KrakenRole.USER.name())
        // Storage
        .pathMatchers("/files/**").hasAnyAuthority(USER.name())
        .pathMatchers("/user-events/**").hasAnyAuthority(API.name())
        // Test only
        .pathMatchers("/test/user/**").hasAnyAuthority(USER.name())
        .pathMatchers("/test/admin/**").hasAnyAuthority(ADMIN.name())
        .anyExchange().denyAll()
        .and()
        .oauth2ResourceServer(oAuth2Spec ->
            oAuth2Spec
                .bearerTokenConverter(tokenConverter)
                .jwt()
                .jwtAuthenticationConverter(converter)
        );
    return http.build();
  }
}
