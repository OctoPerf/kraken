package com.kraken.runtime.context;

import com.kraken.runtime.context.api.FilterScopedEntries;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringFilterScopedEntries implements FilterScopedEntries {

  @Override
  public List<ExecutionEnvironmentEntry> apply(final String hostId, final List<ExecutionEnvironmentEntry> entries) {
    return entries
        .stream()
        .filter(entry -> entry.getScope().equals(hostId) || entry.getScope().equals(""))
        .collect(Collectors.toUnmodifiableList());
  }

}
