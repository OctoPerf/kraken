package com.octoperf.kraken.runtime.context.api;

import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;

import java.util.List;
import java.util.function.Function;

/**
 * Sort entries by ExecutionEnvironmentEntrySource (BACKEND first, USER last) and scope ("" global scope first, "hostId" last)
 */
public interface SortExecutionEnvironmentEntries extends Function<List<ExecutionEnvironmentEntry>, List<ExecutionEnvironmentEntry>> {

}
