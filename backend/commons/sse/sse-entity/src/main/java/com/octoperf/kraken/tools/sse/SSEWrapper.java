package com.octoperf.kraken.tools.sse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@ExcludeFromObfuscation
public class SSEWrapper {
  String type;
  Object value;

  @JsonCreator
  SSEWrapper(
      @NonNull @JsonProperty("type") final String type,
      @NonNull @JsonProperty("value") final Object value) {
    super();
    this.type = type;
    this.value = value;
  }
}
