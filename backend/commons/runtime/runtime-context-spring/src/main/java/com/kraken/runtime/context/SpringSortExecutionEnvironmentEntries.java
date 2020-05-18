package com.kraken.runtime.context;

import com.kraken.runtime.context.api.SortExecutionEnvironmentEntries;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringSortExecutionEnvironmentEntries implements SortExecutionEnvironmentEntries {

  @Override
  public List<ExecutionEnvironmentEntry> apply(List<ExecutionEnvironmentEntry> entries) {
    Comparator<ExecutionEnvironmentEntry> comparator = Comparator
        .<ExecutionEnvironmentEntry>comparingInt(entry -> entry.getFrom().ordinal())
        .thenComparing(ExecutionEnvironmentEntry::getScope);
    return entries.stream().sorted(comparator).collect(Collectors.toUnmodifiableList());
  }
}
