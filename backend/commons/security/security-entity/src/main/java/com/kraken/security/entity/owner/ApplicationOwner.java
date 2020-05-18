package com.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ApplicationOwner implements Owner {

  String applicationId;

  @JsonCreator
  ApplicationOwner(
      @NonNull @JsonProperty("applicationId") final String applicationId
  ) {
    super();
    this.applicationId = applicationId;
  }

  @Override
  public OwnerType getType() {
    return OwnerType.APPLICATION;
  }
}
