package com.octoperf.kraken.runtime.context.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owned;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CancelContext  implements Owned {
  Owner owner;
  String taskId;
  TaskType taskType;

  @JsonCreator
  CancelContext(
      @NonNull @JsonProperty("owner") final Owner owner,
      @NonNull @JsonProperty("taskId") final String taskId,
      @NonNull @JsonProperty("taskType") final TaskType taskType
  ) {
    super();
    this.owner = owner;
    this.taskId = taskId;
    this.taskType = taskType;
  }
}
