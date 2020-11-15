package com.octoperf.kraken.security.authentication.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.security.authentication.api.AuthenticationMode;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Value;

import java.util.Optional;

import static com.google.common.base.Strings.nullToEmpty;

@Value
public final class AuthenticatedClientBuildOrder {

  public static final AuthenticatedClientBuildOrder NOOP = AuthenticatedClientBuildOrder.builder()
      .noop()
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

  public static AuthenticatedClientBuildOrderBuilder builder() {
    return new AuthenticatedClientBuildOrderBuilder();
  }

  public AuthenticatedClientBuildOrderBuilder toBuilder() {
    return new AuthenticatedClientBuildOrderBuilder().mode(this.mode).userId(this.userId).projectId(this.projectId).applicationId(this.applicationId);
  }

  public static class AuthenticatedClientBuildOrderBuilder {
    private AuthenticationMode mode;
    private String userId;
    private String projectId;
    private String applicationId;

    AuthenticatedClientBuildOrderBuilder() {
    }

    public AuthenticatedClientBuildOrderBuilder noop() {
      this.mode = AuthenticationMode.NOOP;
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder session(final Owner owner) {
      this.mode = AuthenticationMode.SESSION;
      this.applicationId = owner.getApplicationId();
      this.projectId = owner.getProjectId();
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder impersonate(final Owner owner) {
      this.mode = AuthenticationMode.IMPERSONATE;
      this.applicationId = owner.getApplicationId();
      this.projectId = owner.getProjectId();
      this.userId = owner.getUserId();
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder serviceAccount(final Owner owner) {
      this.mode = AuthenticationMode.SERVICE_ACCOUNT;
      this.applicationId = owner.getApplicationId();
      this.projectId = owner.getProjectId();
      this.userId = owner.getUserId();
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder container(final String applicationId, final String projectId) {
      this.mode = AuthenticationMode.CONTAINER;
      this.applicationId = applicationId;
      this.projectId = projectId;
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder mode(final AuthenticationMode mode) {
      this.mode = mode;
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder userId(final String userId) {
      this.userId = userId;
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder projectId(final String projectId) {
      this.projectId = projectId;
      return this;
    }

    public AuthenticatedClientBuildOrderBuilder applicationId(final String applicationId) {
      this.applicationId = applicationId;
      return this;
    }

    public AuthenticatedClientBuildOrder build() {
      return new AuthenticatedClientBuildOrder(mode, userId, projectId, applicationId);
    }

    public String toString() {
      return "AuthenticatedClientBuildOrder.AuthenticatedClientBuildOrderBuilder(mode=" + this.mode + ", userId=" + this.userId + ", projectId=" + this.projectId + ", applicationId=" + this.applicationId + ")";
    }
  }
}
