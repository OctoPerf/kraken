package com.kraken.security.oauth;

// https://blog.ineat-group.com/2019/01/securiser-vos-apis-spring-webflux-avec-keycloak-et-oauth2/

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@EnableWebFluxSecurity
public class OAuthConfiguration {

  @Bean
  Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
    GrantedAuthoritiesExtractor extractor = new GrantedAuthoritiesExtractor();
    return new ReactiveJwtAuthenticationConverterAdapter(extractor);
  }

  static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {
    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
      System.out.println(jwt);
      return super.extractAuthorities(jwt);
    }
  }
}
