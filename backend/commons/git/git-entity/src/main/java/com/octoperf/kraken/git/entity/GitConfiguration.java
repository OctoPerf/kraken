package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GitConfiguration {
  String repositoryUrl;

  @JsonCreator
  GitConfiguration(
      @NonNull @JsonProperty("repositoryUrl") final String repositoryUrl
  ) {
    super();
    this.repositoryUrl = repositoryUrl;
  }
}
