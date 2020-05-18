package com.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class LoginRequest {
  String user;
  String password;
  String email;

  @JsonCreator
  LoginRequest(
      @NonNull @JsonProperty("user") final String user,
      @NonNull @JsonProperty("password") final String password,
      @NonNull @JsonProperty("email") final String email
  ) {
    super();
    this.user = user;
    this.password = password;
    this.email = email;
  }
}
