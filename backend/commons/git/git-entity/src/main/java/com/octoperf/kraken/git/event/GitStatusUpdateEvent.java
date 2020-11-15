package com.octoperf.kraken.git.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Git status must be refreshed
 */
@Value
@Builder(toBuilder = true)
public class GitStatusUpdateEvent implements GitEvent {

  @NonNull Owner owner;

  @JsonCreator
  GitStatusUpdateEvent(
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.owner = owner;
  }
}
