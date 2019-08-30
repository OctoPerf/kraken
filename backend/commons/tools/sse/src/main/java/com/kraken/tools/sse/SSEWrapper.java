package com.kraken.tools.sse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class SSEWrapper<T> {
  String type;
  T value;

  @JsonCreator
  SSEWrapper(
      @JsonProperty("type") final String type,
      @JsonProperty("value") final T value) {
    super();
    this.type = requireNonNull(type);
    this.value = requireNonNull(value);
  }
}
