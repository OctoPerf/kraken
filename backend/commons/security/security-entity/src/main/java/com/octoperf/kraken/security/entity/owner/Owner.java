package com.octoperf.kraken.security.entity.owner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.security.entity.token.KrakenRole;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
public class Owner {

  public static final Owner PUBLIC = Owner.builder()
      .type(OwnerType.PUBLIC)
      .build();

  String userId;
  String projectId;
  String applicationId;
  @EqualsAndHashCode.Exclude
  List<KrakenRole> roles;
  OwnerType type;

  @JsonCreator
  Owner(
      @JsonProperty("userId") final String userId,
      @JsonProperty("projectId") final String projectId,
      @JsonProperty("applicationId") final String applicationId,
      @JsonProperty("roles") final List<KrakenRole> roles,
      @NonNull @JsonProperty("type") final OwnerType type
  ) {
    super();
    this.userId = nullToEmpty(userId);
    this.applicationId = nullToEmpty(applicationId);
    this.projectId = nullToEmpty(projectId);
    this.roles = Optional.ofNullable(roles).orElse(ImmutableList.of());
    this.type = type;
  }

}
