package com.octoperf.kraken.project.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.project.entity.Project;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class DeleteProjectEvent implements ProjectEvent {

  Project project;
  Owner owner;

  @JsonCreator
  DeleteProjectEvent(
      @NonNull @JsonProperty("project") final Project project,
      @NonNull @JsonProperty("owner") final Owner owner
  ) {
    super();
    this.project = project;
    this.owner = owner;
  }
}
