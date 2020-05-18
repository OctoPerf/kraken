package com.kraken.runtime.context;

import com.kraken.runtime.context.api.FilterScopedEntries;
import com.kraken.runtime.context.api.MapExecutionEnvironmentEntries;
import com.kraken.runtime.context.api.SortExecutionEnvironmentEntries;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.copyOf;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class SpringMapExecutionEnvironmentEntries implements MapExecutionEnvironmentEntries {
  @NonNull FilterScopedEntries filter;
  @NonNull SortExecutionEnvironmentEntries sort;

  @Override
  public Map<String, String> apply(final String hostId, final List<ExecutionEnvironmentEntry> entries) {
    final var scopedEntries = this.filter.apply(hostId, entries);
    final var sortedEntries = this.sort.apply(scopedEntries);
    final var map = new LinkedHashMap<String, String>();
    sortedEntries.forEach(e -> map.put(e.getKey(), e.getValue()));
    return copyOf(map);
  }

}
