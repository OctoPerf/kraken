package com.kraken.security.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KrakenFederatedIdentity {
  String identityProvider;
  String userId;
  String userName;

  @JsonCreator
  KrakenFederatedIdentity(
      @JsonProperty("identityProvider") final String identityProvider,
      @JsonProperty("userId") final String userId,
      @JsonProperty("userName") final String userName
  ) {
    super();
    this.identityProvider = identityProvider;
    this.userId = userId;
    this.userName = userName;
  }
}
