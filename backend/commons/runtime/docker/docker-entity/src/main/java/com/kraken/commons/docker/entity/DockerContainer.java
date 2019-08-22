package com.kraken.commons.docker.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class DockerContainer {

  String id;
  String name;
  String image;
  String status;
  Object full;

  @JsonCreator
  DockerContainer(
    @JsonProperty("id") final String id,
    @JsonProperty("name") final String name,
    @JsonProperty("image") final String image,
    @JsonProperty("status") final String status,
    @JsonProperty("full") final Object full) {
    super();
    this.id = requireNonNull(id);
    this.name = requireNonNull(name);
    this.image = requireNonNull(image);
    this.status = requireNonNull(status);
    this.full = requireNonNull(full);
  }
}
