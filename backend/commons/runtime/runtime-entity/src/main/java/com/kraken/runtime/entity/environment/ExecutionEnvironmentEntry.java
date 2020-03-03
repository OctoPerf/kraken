package com.kraken.runtime.entity.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class ExecutionEnvironmentEntry {
  @NonNull String scope; // "" for global or a host ID
  @NonNull ExecutionEnvironmentEntrySource from;
  @NonNull String key;
  @NonNull String value;

  @JsonCreator
  ExecutionEnvironmentEntry(
      @JsonProperty("scope") final String scope,
      @JsonProperty("from") final ExecutionEnvironmentEntrySource from,
      @JsonProperty("environment") final String key,
      @JsonProperty("hosts") final String value) {
    super();
    this.scope = requireNonNull(scope);
    this.from = requireNonNull(from);
    this.key = requireNonNull(key);
    this.value = requireNonNull(value);
  }
}
