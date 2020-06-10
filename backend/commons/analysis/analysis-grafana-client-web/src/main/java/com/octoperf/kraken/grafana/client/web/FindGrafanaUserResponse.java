package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
class FindGrafanaUserResponse {
  Long id;
  Long orgId;

  @JsonCreator
  FindGrafanaUserResponse(
      @NonNull @JsonProperty("id") final Long id,
      @NonNull @JsonProperty("orgId") final Long orgId
  ) {
    super();
    this.id = id;
    this.orgId = orgId;
  }
}