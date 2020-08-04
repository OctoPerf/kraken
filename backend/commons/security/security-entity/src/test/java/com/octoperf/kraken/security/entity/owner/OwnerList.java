package com.octoperf.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class OwnerList {

  List<Owner> owners;

  @JsonCreator
  OwnerList(
      @NonNull @JsonProperty("owners") final List<Owner> owners
  ) {
    super();
    this.owners = owners;
  }

}
