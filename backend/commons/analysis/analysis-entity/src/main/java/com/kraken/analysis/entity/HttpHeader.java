package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class HttpHeader {

  String key;
  String value;

  @JsonCreator
  HttpHeader(
      @NonNull @JsonProperty("key") final String key,
      @NonNull @JsonProperty("value") final String value
  ) {
    super();
    this.key = key;
    this.value = value;
  }
}
