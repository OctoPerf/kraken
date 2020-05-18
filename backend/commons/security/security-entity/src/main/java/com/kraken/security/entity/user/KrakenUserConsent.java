package com.kraken.security.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KrakenUserConsent {
  String clientId;
  Long createdDate;
  List<String> grantedClientScopes;
  Long lastUpdatedDate;

  @JsonCreator
  KrakenUserConsent(
      @JsonProperty("clientId") final String clientId,
      @JsonProperty("createdDate") final Long createdDate,
      @JsonProperty("grantedClientScopes") final List<String> grantedClientScopes,
      @JsonProperty("lastUpdatedDate") final Long lastUpdatedDate
  ) {
    super();
    this.clientId = clientId;
    this.createdDate = createdDate;
    this.grantedClientScopes = grantedClientScopes;
    this.lastUpdatedDate = lastUpdatedDate;
  }

}
