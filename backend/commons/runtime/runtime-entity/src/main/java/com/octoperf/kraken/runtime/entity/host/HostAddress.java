package com.octoperf.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class HostAddress {
  String address;
  String type;

  @JsonCreator
  HostAddress(
      @NonNull @JsonProperty("address") final String address,
      @NonNull @JsonProperty("type") final String type) {
    super();
    this.address = address;
    this.type = type;
  }
}
