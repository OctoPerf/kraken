package com.kraken.security.entity.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;


@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenToken {

  String accessToken;
  String refreshToken;
  Long expiresIn; // seconds
  Long refreshExpiresIn; // seconds

  @JsonCreator
  KrakenToken(
      @NonNull @JsonProperty("access_token") final String accessToken,
      @NonNull @JsonProperty("refresh_token") final String refreshToken,
      @NonNull @JsonProperty("expires_in") final Long expiresIn,
      @NonNull @JsonProperty("refresh_expires_in") final Long refreshExpiresIn
  ) {
    super();
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.refreshExpiresIn = refreshExpiresIn;
  }

}