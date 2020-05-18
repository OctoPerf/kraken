package com.kraken.runtime.context.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.entity.owner.Owned;
import com.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
public class ExecutionContext implements Owned {
  Owner owner;
  String taskId;
  TaskType taskType;
  String description;
  //  Key: hostId, Value; template specific to this host
  Map<String, String> templates;

  @JsonCreator
  ExecutionContext(
      @NonNull @JsonProperty("owner") final Owner owner,
      @NonNull @JsonProperty("taskId") final String taskId,
      @NonNull @JsonProperty("taskType") final TaskType taskType,
      @NonNull @JsonProperty("description") final String description,
      @NonNull @JsonProperty("templates") final Map<String, String> templates
  ) {
    super();
    this.owner = owner;
    this.taskId = taskId;
    this.taskType = taskType;
    this.description = description;
    this.templates = templates;
  }
}
