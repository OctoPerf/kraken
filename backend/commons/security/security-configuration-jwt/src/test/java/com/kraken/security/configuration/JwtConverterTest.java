package com.kraken.security.configuration;

import com.google.common.collect.ImmutableList;
import com.kraken.security.configuration.entity.KrakenUser;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtConverterTest {

  JwtConverter converter;

  @Before
  public void setUp() {
    converter = new JwtConverter();
  }

  @Test
  public void shouldConvertNoGroup() {
    final var roles = new JSONArray();
    roles.appendElement("USER");
    final var realmAccess = new JSONObject();
    realmAccess.appendField("roles", roles);
    final var groups = new JSONArray();
    groups.appendElement("/default-kraken");

    final var jwt = Jwt.withTokenValue("token")
        .claim("preferred_username", "username")
        .claim("user_groups", groups)
        .claim("realm_access", realmAccess)
        .subject("userId")
        .issuedAt(Instant.EPOCH)
        .expiresAt(Instant.EPOCH.plusMillis(1))
        .header("foo", "bar")
        .build();

    final var result = converter.convert(jwt);
    assertThat(result).isNotNull();
    final var token = result.block();
    assertThat(token).isNotNull();
    assertThat(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableList())).isEqualTo(ImmutableList.of("USER"));
    assertThat(token.getDetails()).isEqualTo(KrakenUser.builder()
        .userId("userId")
        .currentGroup("")
        .username("username")
        .groups(of("/default-kraken"))
        .roles(of("USER"))
        .build());
  }

  @Test
  public void shouldConvertWithGroup() {
    final var roles = new JSONArray();
    roles.appendElement("USER");
    final var realmAccess = new JSONObject();
    realmAccess.appendField("roles", roles);
    final var groups = new JSONArray();
    groups.appendElement("/default-kraken");

    final var jwt = Jwt.withTokenValue("token")
        .claim("preferred_username", "username")
        .claim("user_groups", groups)
        .claim("realm_access", realmAccess)
        .claim("current_group", "/default-kraken")
        .subject("userId")
        .issuedAt(Instant.EPOCH)
        .expiresAt(Instant.EPOCH.plusMillis(1))
        .header("foo", "bar")
        .build();

    final var result = converter.convert(jwt);
    assertThat(result).isNotNull();
    final var token = result.block();
    assertThat(token).isNotNull();
    assertThat(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableList())).isEqualTo(ImmutableList.of("USER"));
    assertThat(token.getDetails()).isEqualTo(KrakenUser.builder()
        .userId("userId")
        .currentGroup("/default-kraken")
        .username("username")
        .groups(of("/default-kraken"))
        .roles(of("USER"))
        .build());
  }

  @Test(expected = AuthenticationServiceException.class)
  public void shouldNotConvertGroupMismatch() {
    final var roles = new JSONArray();
    roles.appendElement("USER");
    final var realmAccess = new JSONObject();
    realmAccess.appendField("roles", roles);
    final var groups = new JSONArray();
    groups.appendElement("/default-kraken");

    final var jwt = Jwt.withTokenValue("token")
        .claim("preferred_username", "username")
        .claim("user_groups", groups)
        .claim("realm_access", realmAccess)
        .claim("current_group", "other group")
        .subject("userId")
        .issuedAt(Instant.EPOCH)
        .expiresAt(Instant.EPOCH.plusMillis(1))
        .header("foo", "bar")
        .build();

    final var result = converter.convert(jwt);
    assertThat(result).isNotNull();
    result.block();
  }

}