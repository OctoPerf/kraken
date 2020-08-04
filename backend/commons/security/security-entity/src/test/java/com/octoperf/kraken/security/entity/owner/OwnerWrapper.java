package com.octoperf.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OwnerWrapper {

  Owner owner;

  @JsonCreator
  OwnerWrapper(
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.owner = owner;
  }

}
