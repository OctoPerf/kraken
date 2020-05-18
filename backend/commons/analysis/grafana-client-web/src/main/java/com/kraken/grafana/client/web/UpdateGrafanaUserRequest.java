package com.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class UpdateGrafanaUserRequest {
  String name;
  String email;
  String login;

  @JsonCreator
  UpdateGrafanaUserRequest(
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("email") final String email,
      @NonNull @JsonProperty("login") final String login
  ) {
    super();
    this.name = name;
    this.email = email;
    this.login = login;
  }
}