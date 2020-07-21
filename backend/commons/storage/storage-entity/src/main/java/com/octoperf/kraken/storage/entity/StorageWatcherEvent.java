package com.octoperf.kraken.storage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class StorageWatcherEvent {

  StorageNode node;
  String event;

  @JsonCreator
  StorageWatcherEvent(
      @NonNull @JsonProperty("node") final StorageNode node,
      @NonNull @JsonProperty("event") final String event) {
    super();
    this.node = node;
    this.event = event;
  }

}
