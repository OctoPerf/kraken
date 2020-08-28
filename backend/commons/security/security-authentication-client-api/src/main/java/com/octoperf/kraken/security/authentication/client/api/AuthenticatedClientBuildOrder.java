package com.octoperf.kraken.security.authentication.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
public class AuthenticatedClientBuildOrder {

  public static final AuthenticatedClientBuildOrder NOOP = AuthenticatedClientBuildOrder.builder()
      .mode(AuthenticationMode.NOOP)
      .build();

  AuthenticationMode mode;
  String userId;
  String projectId;
  String applicationId;

  @JsonCreator
  AuthenticatedClientBuildOrder(
      @JsonProperty("mode") final AuthenticationMode mode,
      @JsonProperty("userId") final String userId,
      @JsonProperty("projectId") final String projectId,
      @JsonProperty("applicationId") final String applicationId
  ) {
    super();
    this.mode = Optional.ofNullable(mode).orElse(AuthenticationMode.NOOP);
    this.userId = nullToEmpty(userId);
    this.applicationId = nullToEmpty(applicationId);
    this.projectId = nullToEmpty(projectId);
  }

}
