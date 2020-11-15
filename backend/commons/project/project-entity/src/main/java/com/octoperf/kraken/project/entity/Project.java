package com.octoperf.kraken.project.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Project {
  String id;
  String name;
  String applicationId;
  Long createDate;
  Long updateDate;

  @JsonCreator
  Project(
      @NonNull @JsonProperty("id") final String id,
      @NonNull @JsonProperty("name") final String name,
      @NonNull @JsonProperty("applicationId") final String applicationId,
      @NonNull @JsonProperty("createDate") final Long createDate,
      @NonNull @JsonProperty("updateDate") final Long updateDate
      ) {
    super();
    this.id = id;
    this.name = name;
    this.applicationId = applicationId;
    this.createDate = createDate;
    this.updateDate = updateDate;
  }
}
