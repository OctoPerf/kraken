package com.kraken.runtime.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Log {
  String applicationId;
  String id;
  LogType type;
  String text;

  @JsonCreator
  Log(
      @JsonProperty("applicationId") final String applicationId,
      @JsonProperty("id") final String id,
      @JsonProperty("type") final LogType type,
      @JsonProperty("text") final String text) {
    super();
    this.id = requireNonNull(id);
    this.applicationId = requireNonNull(applicationId);
    this.type = requireNonNull(type);
    this.text = requireNonNull(text);
  }
}
