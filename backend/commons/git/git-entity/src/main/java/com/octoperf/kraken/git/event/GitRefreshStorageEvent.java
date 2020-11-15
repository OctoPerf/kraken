package com.octoperf.kraken.git.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Git made modifications on the file system, a refresh should be made to update the UI
 */
@Value
@Builder(toBuilder = true)
public class GitRefreshStorageEvent implements GitEvent {

  @NonNull Owner owner;

  @JsonCreator
  GitRefreshStorageEvent(
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.owner = owner;
  }
}
