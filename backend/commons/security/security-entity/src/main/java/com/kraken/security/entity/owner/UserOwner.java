package com.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.security.entity.token.KrakenRole;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class UserOwner implements Owner {

  String userId;
  @With
  String applicationId;
  List<KrakenRole> roles;

  @JsonCreator
  UserOwner(
      @NonNull @JsonProperty("userId") final String userId,
      @NonNull @JsonProperty("applicationId") final String applicationId,
      @NonNull @JsonProperty("roles") final List<KrakenRole> roles
  ) {
    super();
    this.userId = userId;
    this.applicationId = applicationId;
    this.roles = roles;
  }

  @Override
  public OwnerType getType() {
    return OwnerType.USER;
  }
}
