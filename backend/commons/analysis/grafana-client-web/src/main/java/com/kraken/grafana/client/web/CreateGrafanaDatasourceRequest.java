package com.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class CreateGrafanaDatasourceRequest {

  String name;
  String type;
  String access;
  Boolean isDefault;

  @JsonCreator
  CreateGrafanaDatasourceRequest(
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("type") final String type,
      @NonNull @JsonProperty("access") final String access,
      @NonNull @JsonProperty("isDefault") final Boolean isDefault
      ) {
    super();
    this.name = name;
    this.type = type;
    this.access = access;
    this.isDefault = isDefault;
  }
}