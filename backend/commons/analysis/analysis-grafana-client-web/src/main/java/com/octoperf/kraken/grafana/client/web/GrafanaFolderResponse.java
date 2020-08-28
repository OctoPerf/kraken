package com.octoperf.kraken.grafana.client.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.tools.obfuscation.ExcludeFromObfuscation;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ExcludeFromObfuscation
@SuppressWarnings("squid:CommentedOutCodeLine")
class GrafanaFolderResponse {

  // {"id":4,"uid":"1Pgj9_NMk","title":"test","url":"/grafana/dashboards/f/1Pgj9_NMk/test","hasAcl":false,"canSave":true,"canEdit":true,"canAdmin":true,"createdBy":"559fe423-214e-4061-8651-dfc24b86c057","created":"2020-08-27T14:20:14Z","updatedBy":"559fe423-214e-4061-8651-dfc24b86c057","updated":"2020-08-27T14:20:14Z","version":1}

  Long id;
  String uid;
  String title;

  @JsonCreator
  GrafanaFolderResponse(
      @NonNull @JsonProperty("id") final Long id,
      @NonNull @JsonProperty("uid") final String uid,
      @NonNull @JsonProperty("title") final String title
  ) {
    super();
    this.id = id;
    this.uid = uid;
    this.title = title;
  }
}