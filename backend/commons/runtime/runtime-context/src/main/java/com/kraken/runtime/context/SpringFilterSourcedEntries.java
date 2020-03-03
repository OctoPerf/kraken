package com.kraken.runtime.context;

import com.kraken.runtime.context.api.FilterScopedEntries;
import com.kraken.runtime.context.api.FilterSourcedEntries;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SpringFilterSourcedEntries implements FilterSourcedEntries {

  @NonNull FilterScopedEntries filter;

  @Override
  public List<ExecutionEnvironmentEntry> apply(final String hostId,final  ExecutionEnvironmentEntrySource source, final List<ExecutionEnvironmentEntry> entries) {
    return this.filter.apply(hostId, entries)
        .stream()
        .filter(entry -> entry.getFrom() == source)
        .collect(Collectors.toUnmodifiableList());
  }
  
}
