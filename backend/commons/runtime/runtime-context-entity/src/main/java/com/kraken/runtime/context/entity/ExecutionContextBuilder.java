package com.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.entity.owner.Owned;
import com.kraken.security.entity.owner.Owner;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
