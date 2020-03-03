package com.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableList;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
@AllArgsConstructor
public class ExecutionContextBuilder {
  @NonNull String applicationId;
  @NonNull String taskId;
  @NonNull String taskType;
  @NonNull String description;
  @NonNull String file;
  @NonNull Integer containersCount;
  @NonNull List<String> hostIds;
  @With
  @NonNull List<ExecutionEnvironmentEntry> entries;

  public ExecutionContextBuilder addEntries(List<ExecutionEnvironmentEntry> entries) {
    final var listBuilder = ImmutableList.<ExecutionEnvironmentEntry>builder();
    listBuilder.addAll(this.entries);
    listBuilder.addAll(entries);
    return this.withEntries(listBuilder.build());
  }

}
