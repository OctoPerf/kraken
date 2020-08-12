package com.octoperf.kraken.storage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.entity.owner.Owner;
import com.octoperf.kraken.tools.event.bus.BusEvent;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class StorageWatcherEvent implements BusEvent {

  StorageNode node;
  StorageWatcherEventType type;
  Owner owner;

  @JsonCreator
  StorageWatcherEvent(
      @NonNull @JsonProperty("node") final StorageNode node,
      @NonNull @JsonProperty("type") final StorageWatcherEventType type,
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.node = node;
    this.type = type;
    this.owner = owner;
  }

}
