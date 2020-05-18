package com.kraken.runtime.entity.host;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class HostCapacity {
  String cpu;
  String memory;

  @JsonCreator
  HostCapacity(
      @JsonProperty("cpu") final String cpu,
      @JsonProperty("memory") final String memory) {
    super();
    this.cpu = requireNonNull(cpu);
    this.memory = requireNonNull(memory);
  }
}
