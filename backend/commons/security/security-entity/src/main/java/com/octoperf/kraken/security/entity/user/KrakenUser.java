package com.octoperf.kraken.security.entity.user;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Value
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KrakenUser {
  Map<String, String> access;
  @With
  Map<String, ? extends List<String>> attributes;
  List<KrakenUserConsent> clientConsents;
  Map<String, String> clientRoles;
  Long createdTimestamp;
  List<KrakenCredential> credentials;
  List<String> disableableCredentialTypes;
  String email;
  Boolean emailVerified;
  Boolean enabled;
  Boolean totp;
  List<KrakenFederatedIdentity> federatedIdentities;
  String federationLink;
  String firstName;
  List<String> groups;
  String id;
  String lastName;
  Integer notBefore;
  String origin;
  List<String> realmRoles;
  List<String> requiredActions;
  String self;
  String serviceAccountClientId;
  String username;

  // Warning: every attribute can be null. This bean is used to retrieve a user from Keycloak, update it and sent it back. Do not set default values for null attributes!
  @JsonCreator
  KrakenUser(
      @JsonProperty("access") final Map<String, String> access,
      @JsonProperty("attributes") final Map<String, ? extends List<String>> attributes,
      @JsonProperty("clientConsents") final List<KrakenUserConsent> clientConsents,
      @JsonProperty("clientRoles") final Map<String, String> clientRoles,
      @JsonProperty("createdTimestamp") final Long createdTimestamp,
      @JsonProperty("credentials") final List<KrakenCredential> credentials,
      @JsonProperty("disableableCredentialTypes") final List<String> disableableCredentialTypes,
      @JsonProperty("email") final String email,
      @JsonProperty("emailVerified") final Boolean emailVerified,
      @JsonProperty("enabled") final Boolean enabled,
      @JsonProperty("totp") final Boolean totp,
      @JsonProperty("federatedIdentities") final List<KrakenFederatedIdentity> federatedIdentities,
      @JsonProperty("federationLink") final String federationLink,
      @JsonProperty("firstName") final String firstName,
      @JsonProperty("groups") final List<String> groups,
      @JsonProperty("id") final String id,
      @JsonProperty("lastName") final String lastName,
      @JsonProperty("notBefore") final Integer notBefore,
      @JsonProperty("origin") final String origin,
      @JsonProperty("realmRoles") final List<String> realmRoles,
      @JsonProperty("requiredActions") final List<String> requiredActions,
      @JsonProperty("self") final String self,
      @JsonProperty("serviceAccountClientId") final String serviceAccountClientId,
      @JsonProperty("username") final String username
  ) {
    super();
    this.access = access;
    this.attributes = attributes;
    this.clientConsents = clientConsents;
    this.clientRoles = clientRoles;
    this.createdTimestamp = createdTimestamp;
    this.credentials = credentials;
    this.disableableCredentialTypes = disableableCredentialTypes;
    this.email = email;
    this.emailVerified = emailVerified;
    this.enabled = enabled;
    this.totp = totp;
    this.federatedIdentities = federatedIdentities;
    this.federationLink = federationLink;
    this.firstName = firstName;
    this.groups = groups;
    this.id = id;
    this.lastName = lastName;
    this.notBefore = notBefore;
    this.origin = origin;
    this.realmRoles = realmRoles;
    this.requiredActions = requiredActions;
    this.self = self;
    this.serviceAccountClientId = serviceAccountClientId;
    this.username = username;
  }

  @JsonIgnore
  public String getAttribute(final String attributeName) {
    return this.getAttributes(attributeName).get(0);
  }

  @JsonIgnore
  public List<String> getAttributes(final String attributeName) {
    checkArgument(this.hasAttribute(attributeName));
    return this.attributes.get(attributeName);
  }

  @JsonIgnore
  public Boolean hasAttribute(final String attributeName) {
    return this.attributes != null && this.attributes.containsKey(attributeName) && this.attributes.get(attributeName).size() > 0;
  }
}
