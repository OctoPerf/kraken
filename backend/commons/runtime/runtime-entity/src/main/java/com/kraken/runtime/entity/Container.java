package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Container {
  String id;
  String hostId;
  String hostname;
  String name;
  Long startDate;
  @With
  ContainerStatus status;

  @JsonCreator
  Container(
      @JsonProperty("id") final String id,
      @JsonProperty("hostId") final String hostId,
      @JsonProperty("hostname") final String hostname,
      @JsonProperty("name") final String name,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = requireNonNull(id);
    this.hostId = requireNonNull(hostId);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.hostname = requireNonNull(hostname);
    this.name = requireNonNull(name);
  }
}
