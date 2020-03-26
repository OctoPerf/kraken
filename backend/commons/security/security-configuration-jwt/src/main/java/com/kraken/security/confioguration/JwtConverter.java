package com.kraken.storage.configuration;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@Component
class JwtConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

  @Override
  public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
    final var claims = jwt.getClaims();
    JSONObject realmAccess = (JSONObject) claims.get("realm_access");
    JSONArray roles = (JSONArray) realmAccess.get("roles");
    JSONArray groups = (JSONArray) claims.get("user_groups");
    log.info("Roles: " + roles.stream().map(Object::toString).collect(Collectors.joining(", ")));
    log.info("Groups: " + groups.stream().map(Object::toString).collect(Collectors.joining(", ")));
    final var authorities = roles.stream()
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
    final var token = new JwtAuthenticationToken(jwt, authorities);
    token.setDetails("toto");
    return Mono.just(token);
  }
}
