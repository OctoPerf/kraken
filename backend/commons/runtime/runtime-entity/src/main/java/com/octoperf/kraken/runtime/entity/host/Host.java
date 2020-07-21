package com.octoperf.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.entity.owner.Owned;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Host implements Owned {
  String id;
  String name;
  HostCapacity capacity;
  HostCapacity allocatable;
  List<HostAddress> addresses;
  Owner owner;

  @JsonCreator
  Host(
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("capacity") final HostCapacity capacity,
      @NonNull @JsonProperty("allocatable") final HostCapacity allocatable,
      @NonNull @JsonProperty("addresses") final List<HostAddress> addresses,
      @NonNull @JsonProperty("owner") final Owner owner) {
    super();
    this.id = id;
    this.name = name;
    this.capacity = capacity;
    this.allocatable = allocatable;
    this.addresses = addresses;
    this.owner = owner;
  }
}
