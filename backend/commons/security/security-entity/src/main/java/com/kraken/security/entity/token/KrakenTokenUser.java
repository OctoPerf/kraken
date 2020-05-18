package com.kraken.security.entity.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableList.of;
import static java.util.Optional.ofNullable;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenTokenUser {

  String username;
  String email;
  String userId;
  List<KrakenRole> roles;
  List<String> groups;
  String currentGroup;
  Instant expirationTime;
  Instant issuedAt;

  @JsonCreator
  KrakenTokenUser(
      @NonNull @JsonProperty("exp") final Long expirationTime,
      @NonNull @JsonProperty("iat") final Long issuedAt,
      @NonNull @JsonProperty("sub") final String userId,
      @JsonProperty("preferred_username") final String username,
      @JsonProperty("email") final String email,
      @JsonProperty("realm_access") final Map<String, List<String>> realmAccess,
      @JsonProperty("user_groups") final List<String> groups,
      @JsonProperty("current_group") final String currentGroup
  ) {
    super();
    this.expirationTime = Instant.ofEpochSecond(expirationTime);
    this.issuedAt = Instant.ofEpochSecond(issuedAt);
    this.userId = userId;
    this.username = nullToEmpty(username);
    this.email = nullToEmpty(email);
    this.roles = ofNullable(ofNullable(realmAccess)
        .orElse(ImmutableMap.of("roles", of())).get("roles"))
        .orElse(of())
        .stream().filter(role -> Arrays.stream(KrakenRole.values()).anyMatch(krakenRole -> krakenRole.toString().equals(role)))
        .map(KrakenRole::valueOf).collect(Collectors.toUnmodifiableList());
    this.groups = ofNullable(groups).orElse(of());
    this.currentGroup = nullToEmpty(currentGroup);
  }

  @Builder(toBuilder = true)
  KrakenTokenUser(
      @NonNull String username,
      @NonNull String email,
      @NonNull String userId,
      @NonNull List<KrakenRole> roles,
      @NonNull List<String> groups,
      @NonNull String currentGroup,
      @NonNull Instant expirationTime,
      @NonNull Instant issuedAt
  ) {
    super();
    this.username = username;
    this.email = email;
    this.userId = userId;
    this.roles = roles;
    this.groups = groups;
    this.currentGroup = currentGroup;
    this.expirationTime = expirationTime;
    this.issuedAt = issuedAt;
  }
}
