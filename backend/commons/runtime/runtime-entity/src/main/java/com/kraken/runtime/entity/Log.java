package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Log {
  String id;
  String applicationId;
  String text;

  @JsonCreator
  Log(
      @JsonProperty("id") final String id,
      @JsonProperty("applicationId") final String applicationId,
      @JsonProperty("text") final String text) {
    super();
    this.id = requireNonNull(id);
    this.applicationId = requireNonNull(applicationId);
    this.text = requireNonNull(text);
  }
}
