package com.octoperf.kraken.git.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GitCredentials {
  String privateKey;
  String publicKey;

  @JsonCreator
  GitCredentials(
      @NonNull @JsonProperty("privateKey") final String privateKey,
      @NonNull @JsonProperty("publicKey") final String publicKey
  ) {
    super();
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }
}
