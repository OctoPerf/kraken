package com.kraken.commons.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

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
  String runDescription;
  ResultType type;

  @JsonCreator
  Result(
      @JsonProperty("id") final String id,
      @JsonProperty("startDate") final Long startDate,
      @JsonProperty("endDate") final Long endDate,
      @JsonProperty("status") final ResultStatus status,
      @JsonProperty("runDescription") final String runDescription,
      @JsonProperty("type") final ResultType type) {
    super();
    this.id = requireNonNull(id);
    this.startDate = requireNonNull(startDate);
    this.endDate = requireNonNull(endDate);
    this.status = requireNonNull(status);
    this.runDescription = requireNonNull(runDescription);
    this.type = requireNonNull(type);
  }

}
