package com.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class Result {
  String id;
  Long startDate;
  @With
  Long endDate;
  @With
  ResultStatus status;
  String description;
  ResultType type;

  @JsonCreator
  Result(
      @JsonProperty("id") final String id,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("endDate") final Long endDate,
      @JsonProperty("status") final ResultStatus status,
      @JsonProperty("description") final String description,
      @JsonProperty("type") final ResultType type) {
    super();
    this.id = requireNonNull(id);
    this.startDate = requireNonNull(startDate);
    this.endDate = requireNonNull(endDate);
    this.status = requireNonNull(status);
    this.description = requireNonNull(description);
    this.type = requireNonNull(type);
  }

}
