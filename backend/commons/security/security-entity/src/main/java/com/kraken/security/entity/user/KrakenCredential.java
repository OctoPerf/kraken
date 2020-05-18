package com.kraken.security.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KrakenCredential {

  Long createdDate;
  String credentialData;
  String id;
  Integer priority;
  String secretData;
  Boolean temporary;
  String type;
  String userLabel;
  String value;

  @JsonCreator
  KrakenCredential(
      @JsonProperty("createdDate") final Long createdDate,
      @JsonProperty("credentialData") final String credentialData,
      @NonNull @JsonProperty("id") final String id,
      @JsonProperty("priority") final Integer priority,
      @JsonProperty("secretData") final String secretData,
      @JsonProperty("temporary") final Boolean temporary,
      @JsonProperty("type") final String type,
      @JsonProperty("userLabel") final String userLabel,
      @JsonProperty("value") final String value
  ) {
    super();
    this.createdDate = ofNullable(createdDate).orElse(0L);
    this.credentialData = nullToEmpty(credentialData);
    this.id = id;
    this.priority = ofNullable(priority).orElse(0);
    this.secretData = nullToEmpty(secretData);
    this.temporary = ofNullable(temporary).orElse(false);
    this.type = nullToEmpty(type);
    this.userLabel = nullToEmpty(userLabel);
    this.value = nullToEmpty(value);
  }
}
