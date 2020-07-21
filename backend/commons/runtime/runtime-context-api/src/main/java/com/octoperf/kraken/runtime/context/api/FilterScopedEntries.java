package com.octoperf.kraken.runtime.context.api;

import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;

import java.util.List;

public interface FilterScopedEntries {

  List<ExecutionEnvironmentEntry> apply(String hostId, List<ExecutionEnvironmentEntry> entries);

}
