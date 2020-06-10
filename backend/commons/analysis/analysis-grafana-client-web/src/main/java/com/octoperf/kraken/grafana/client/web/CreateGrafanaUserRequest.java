package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class CreateGrafanaUserRequest {
  String name;
  String email;
  String login;
  String password;

  @JsonCreator
  CreateGrafanaUserRequest(
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("email") final String email,
      @NonNull @JsonProperty("login") final String login,
      @NonNull @JsonProperty("password") final String password
  ) {
    super();
    this.name = name;
    this.email = email;
    this.login = login;
    this.password = password;
  }
}