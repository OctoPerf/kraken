package com.kraken.runtime.context.api;

import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;

import java.util.List;

public interface FilterScopedEntries {

  List<ExecutionEnvironmentEntry> apply(String hostId, List<ExecutionEnvironmentEntry> entries);

}
