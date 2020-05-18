package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GrafanaLogin {
  String session;
  String url;

  @JsonCreator
  GrafanaLogin(
      @NonNull @JsonProperty("session") final String session,
      @NonNull @JsonProperty("url") final String url) {
    super();
    this.session = session;
    this.url = url;
  }

}