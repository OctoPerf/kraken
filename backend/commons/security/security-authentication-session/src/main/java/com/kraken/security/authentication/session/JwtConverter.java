package com.kraken.security.authentication.session;

import com.kraken.security.decoder.api.TokenDecoder;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class JwtConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

  @NonNull TokenDecoder decoder;

  @Override
  public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
    final var claims = jwt.getClaims();
    final var realmAccess = (JSONObject) claims.get("realm_access");
    final var roles = (JSONArray) realmAccess.get("roles");
    final var authorities = roles.stream()
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
    final var token = new JwtAuthenticationToken(jwt, authorities);
    try {
      final var user = decoder.decode(jwt.getTokenValue());
      token.setDetails(user);
    } catch (Exception e) {
      return Mono.error(new AuthenticationServiceException("Token decoding failed", e));
    }
    return Mono.just(token);
  }
}
