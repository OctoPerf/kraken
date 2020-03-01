package com.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Host {
  String id;
  String name;
  HostCapacity capacity;
  HostCapacity allocatable;
  List<HostAddress> addresses;

  @JsonCreator
  Host(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("capacity") final HostCapacity capacity,
      @JsonProperty("allocatable") final HostCapacity allocatable,
      @JsonProperty("addresses") final List<HostAddress> addresses) {
    super();
    this.id = requireNonNull(id);
    this.name = requireNonNull(name);
    this.capacity = requireNonNull(capacity);
    this.allocatable = requireNonNull(allocatable);
    this.addresses = requireNonNull(addresses);
  }
}
