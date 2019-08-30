package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Container {
  String id;
  String name;
  Long startDate;
  @Wither
  ContainerStatus status;
  String type;

  @JsonCreator
  Container(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("status") final ContainerStatus status,
      @JsonProperty("type") final String type) {
    super();
    this.id = requireNonNull(id);
    this.startDate = requireNonNull(startDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
    this.type = requireNonNull(type);
  }
}
