package com.octoperf.kraken.analysis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

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
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("startDate") final Long startDate,
      @NonNull @JsonProperty("endDate") final Long endDate,
      @NonNull @JsonProperty("status") final ResultStatus status,
      @NonNull @JsonProperty("description") final String description,
      @NonNull @JsonProperty("type") final ResultType type) {
    super();
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
    this.description = description;
    this.type = type;
  }

}
