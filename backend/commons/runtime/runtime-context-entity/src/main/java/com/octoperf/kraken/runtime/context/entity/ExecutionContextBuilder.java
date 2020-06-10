package com.octoperf.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.entity.owner.Owned;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.*;

import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ExecutionContextBuilder  implements Owned {
  @NonNull Owner owner;
  @NonNull String taskId;
  @NonNull TaskType taskType;
  @NonNull String description;
  @NonNull String file;
  @NonNull Integer containersCount;
  @NonNull List<String> hostIds;
  @With(AccessLevel.PRIVATE)
  @NonNull List<ExecutionEnvironmentEntry> entries;

  public ExecutionContextBuilder addEntries(List<ExecutionEnvironmentEntry> entries) {
    final var listBuilder = ImmutableList.<ExecutionEnvironmentEntry>builder();
    listBuilder.addAll(this.entries);
    listBuilder.addAll(entries);
    return this.withEntries(listBuilder.build());
  }

}
