package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
class UpdateGrafanaOrganizationRequest {
  String name;

  @JsonCreator
  UpdateGrafanaOrganizationRequest(
      @NonNull @JsonProperty("name") final String name
  ) {
    super();
    this.name = name;
  }
}