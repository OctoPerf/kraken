package com.kraken.runtime.context.api;

import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public interface MapExecutionEnvironmentEntries extends BiFunction<String, List<ExecutionEnvironmentEntry>, Map<String, String>> {

}
