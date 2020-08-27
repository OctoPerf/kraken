package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
class CreateGrafanaFolderRequest {
  String uid;
  String title;

  @JsonCreator
  CreateGrafanaFolderRequest(
      @NonNull @JsonProperty("uid") final String uid,
      @NonNull @JsonProperty("title") final String title
  ) {
    super();
    this.uid = uid;
    this.title = title;
  }
}