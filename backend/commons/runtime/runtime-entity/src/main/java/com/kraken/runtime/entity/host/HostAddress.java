package com.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class HostAddress {
  String address;
  String type;

  @JsonCreator
  HostAddress(
      @JsonProperty("address") final String address,
      @JsonProperty("type") final String type) {
    super();
    this.address = requireNonNull(address);
    this.type = requireNonNull(type);
  }
}
