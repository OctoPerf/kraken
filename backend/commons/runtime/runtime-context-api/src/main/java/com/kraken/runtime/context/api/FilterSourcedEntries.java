package com.kraken.runtime.context.api;

import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;

import java.util.List;

public interface FilterSourcedEntries {

  List<ExecutionEnvironmentEntry> apply(String hostId, ExecutionEnvironmentEntrySource source, List<ExecutionEnvironmentEntry> entries);

}
