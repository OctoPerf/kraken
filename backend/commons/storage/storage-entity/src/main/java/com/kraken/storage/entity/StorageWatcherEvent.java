package com.kraken.storage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class StorageWatcherEvent {

  StorageNode node;
  String event;

  @JsonCreator
  StorageWatcherEvent(
    @JsonProperty("node") final StorageNode node,
    @JsonProperty("event") final String event) {
    super();
    this.node = requireNonNull(node);
    this.event = requireNonNull(event);
  }

}
