package com.kraken.security.configuration;

import com.kraken.security.configuration.entity.KrakenRole;
import com.kraken.security.configuration.entity.KrakenUser;
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
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;
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
    final var authorities = roles.stream()
        .map(Object::toString)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    final var userId = jwt.getSubject();
    final var user = KrakenUser.builder()
        .roles(roles.stream().map(Object::toString).filter(role -> Arrays.stream(KrakenRole.values()).anyMatch(krakenRole -> krakenRole.toString().equals(role))).map(KrakenRole::valueOf).collect(Collectors.toUnmodifiableList()))
        .groups(groups.stream().map(Object::toString).collect(Collectors.toUnmodifiableList()))
        .userId(userId)
        .username(jwt.getClaimAsString("preferred_username"))
        .currentGroup(Optional.ofNullable(jwt.getClaimAsString("current_group")).orElse(""))
        .build();
    log.info(user.toString());
    if (!user.getCurrentGroup().isEmpty() && user.getGroups().stream().noneMatch(group -> group.equals(user.getCurrentGroup()))) {
      return Mono.error(new AuthenticationServiceException("The current_group does not belong to the connected user"));
    }
    final var token = new JwtAuthenticationToken(jwt, authorities);
    token.setDetails(user);
    return Mono.just(token);
  }
}
