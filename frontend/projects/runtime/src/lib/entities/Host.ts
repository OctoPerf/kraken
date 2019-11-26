package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Host {
  String id;
  String name;
  Map<String, String> capacity;
  List<HostAddress> addresses;

  @JsonCreator
  Host(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("capacity") final Map<String, String> capacity,
      @JsonProperty("addresses") final List<HostAddress> addresses) {
    super();
    this.id = requireNonNull(id);
    this.name = requireNonNull(name);
    this.capacity = requireNonNull(capacity);
    this.addresses = requireNonNull(addresses);
  }
}
