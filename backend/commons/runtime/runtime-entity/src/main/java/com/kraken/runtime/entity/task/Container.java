package com.kraken.runtime.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class Container {
  String id;
  String name;
  String hostId;
  String label;
  Long startDate;
  @With
  ContainerStatus status;

  @JsonCreator
  Container(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("hostId") final String hostId,
      @JsonProperty("label") final String label,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = requireNonNull(id);
    this.hostId = requireNonNull(hostId);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.label = requireNonNull(label);
    this.name = requireNonNull(name);
  }
}
