package com.octoperf.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class HostCapacity {
  String cpu;
  String memory;

  @JsonCreator
  HostCapacity(
      @NonNull @JsonProperty("cpu") final String cpu,
      @NonNull @JsonProperty("memory") final String memory) {
    super();
    this.cpu = cpu;
    this.memory = memory;
  }
}
