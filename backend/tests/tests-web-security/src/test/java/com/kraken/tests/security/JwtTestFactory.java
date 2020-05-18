package com.kraken.tests.security;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface JwtTestFactory {

   JwtTestFactory JWT_FACTORY = (final String token, final List<String> roles, final List<String> groups, final Optional<String> group) -> {
    final var rolesArray = new JSONArray();
    roles.forEach(rolesArray::appendElement);
    final var realmAccess = new JSONObject();
    realmAccess.appendField("roles", rolesArray);
    final var groupsArray = new JSONArray();
    groups.forEach(groupsArray::appendElement);

    final var jwt = Jwt.withTokenValue(token)
        .claim("preferred_username", "username")
        .claim("user_groups", groupsArray)
        .claim("realm_access", realmAccess)
        .subject("userId")
        .issuedAt(Instant.EPOCH)
        .expiresAt(Instant.EPOCH.plusMillis(1))
        .header("foo", "bar");

    group.ifPresent(g -> jwt.claim("current_group", g));
    return jwt.build();
  };

  Jwt create(String token, List<String> roles, List<String> groups, Optional<String> group);

}
