package com.octoperf.kraken.storage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

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
      @NonNull @JsonProperty("path") final String path,
      @NonNull @JsonProperty("type") final StorageNodeType type,
      @NonNull @JsonProperty("depth") final Integer depth,
      @NonNull @JsonProperty("length") final Long length,
      @NonNull @JsonProperty("lastModified") final Long lastModified) {
    super();
    this.path = path;
    this.type = type;
    this.depth = depth;
    this.length = length;
    this.lastModified = lastModified;
  }

  @JsonIgnore
  public boolean notRoot() {
    return !this.path.isEmpty();
  }
}
