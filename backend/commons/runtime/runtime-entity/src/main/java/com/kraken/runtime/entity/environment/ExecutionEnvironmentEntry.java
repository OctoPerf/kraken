package com.kraken.runtime.entity.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ExecutionEnvironmentEntry {
  @NonNull String scope; // "" for global or a host ID
  @NonNull ExecutionEnvironmentEntrySource from;
  @NonNull String key;
  @NonNull String value;

  @JsonCreator
  ExecutionEnvironmentEntry(
      @NonNull @JsonProperty("scope") final String scope,
      @NonNull @JsonProperty("from") final ExecutionEnvironmentEntrySource from,
      @NonNull @JsonProperty("key") final String key,
      @NonNull @JsonProperty("value") final String value) {
    super();
    this.scope = scope;
    this.from = from;
    this.key = key;
    this.value = value;
  }
}
