package com.kraken.commons.docker.client.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class DockerImage {

  String id;
  String name;
  String tag;
  String created;
  Long size;
  Object full;

  @JsonCreator
  DockerImage(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("tag") final String tag,
      @JsonProperty("created") final String created,
      @JsonProperty("size") final Long size,
      @JsonProperty("full") final Object full) {
    super();
    this.id = requireNonNull(id);
    this.name = requireNonNull(name);
    this.tag = requireNonNull(tag);
    this.created = requireNonNull(created);
    this.size = requireNonNull(size);
    this.full = requireNonNull(full);
  }
}
