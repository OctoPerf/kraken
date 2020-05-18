package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class HttpHeader {

  String key;
  String value;

  @JsonCreator
  HttpHeader(
      @JsonProperty("key") final String key,
      @JsonProperty("value") final String value
  ) {
    super();
    this.key = requireNonNull(key);
    this.value = requireNonNull(value);
  }
}
