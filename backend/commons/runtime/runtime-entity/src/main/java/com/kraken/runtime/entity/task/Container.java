package com.kraken.runtime.entity.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

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
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("hostId") final String hostId,
      @NonNull @JsonProperty("label") final String label,
      @NonNull @JsonProperty("startDate") final Long startDate,
      @NonNull @JsonProperty("status") final ContainerStatus status) {
    super();
    this.id = id;
    this.hostId = hostId;
    this.startDate = startDate;
    this.status = status;
    this.label = label;
    this.name = name;
  }
}
