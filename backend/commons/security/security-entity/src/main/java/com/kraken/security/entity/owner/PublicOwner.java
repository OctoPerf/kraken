package com.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;

@Value
public class PublicOwner implements Owner {

  public final static PublicOwner INSTANCE = new PublicOwner();

  @JsonCreator
  PublicOwner() {
    super();
  }

  @Override
  public OwnerType getType() {
    return OwnerType.PUBLIC;
  }
}
