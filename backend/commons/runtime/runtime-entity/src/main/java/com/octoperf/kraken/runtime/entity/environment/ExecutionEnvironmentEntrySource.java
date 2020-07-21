package com.octoperf.kraken.runtime.entity.environment;

public enum ExecutionEnvironmentEntrySource {
  // By inversed order of importance
  BACKEND,
  TASK_CONFIGURATION,
  FRONTEND,
  USER,
  SECURITY,
}
