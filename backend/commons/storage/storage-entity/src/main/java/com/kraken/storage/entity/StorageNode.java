package com.kraken.storage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class StorageNode {

  String path;
  StorageNodeType type;
  Integer depth;
  Long length;
  Long lastModified;

  @JsonCreator
  StorageNode(
      @JsonProperty("path") final String path,
      @JsonProperty("type") final StorageNodeType type,
      @JsonProperty("depth") final Integer depth,
      @JsonProperty("length") final Long length,
      @JsonProperty("lastModified") final Long lastModified) {
    super();
    this.path = requireNonNull(path);
    this.type = requireNonNull(type);
    this.depth = requireNonNull(depth);
    this.length = requireNonNull(length);
    this.lastModified = requireNonNull(lastModified);
  }


  @JsonIgnore
  public boolean notRoot() {
    return !this.path.isEmpty();
  }
}
