package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Result {
  String id;
  Long startDate;
  @Wither
  Long endDate;
  @Wither
  ResultStatus status;
  String name;
  Optional<String> description;
  ResultType type;

  @JsonCreator
  Result(
      @JsonProperty("id") final String id,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("endDate") final Long endDate,
      @JsonProperty("status") final ResultStatus status,
      @JsonProperty("name") final String name,
      @JsonProperty("description") final Optional<String> description,
      @JsonProperty("type") final ResultType type) {
    super();
    this.id = requireNonNull(id);
    this.startDate = requireNonNull(startDate);
    this.endDate = requireNonNull(endDate);
    this.status = requireNonNull(status);
    this.name = requireNonNull(name);
    this.description = requireNonNull(description);
    this.type = requireNonNull(type);
  }

}
