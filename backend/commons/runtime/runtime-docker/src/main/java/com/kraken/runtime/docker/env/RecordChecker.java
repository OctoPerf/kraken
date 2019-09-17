package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Component
class RecordChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireNonNull(environment.get("KRAKEN_VERSION"));
    requireNonNull(environment.get("KRAKEN_GATLING_SIMULATION_CLASS"));
    requireNonNull(environment.get("KRAKEN_GATLING_SIMULATION_PACKAGE"));
    requireNonNull(environment.get("KRAKEN_DESCRIPTION"));
    requireNonNull(environment.get("KRAKEN_TASK_ID"));
    requireNonNull(environment.get("KRAKEN_ANALYSIS_URL"));
    requireNonNull(environment.get("KRAKEN_RUNTIME_URL"));
    requireNonNull(environment.get("KRAKEN_STORAGE_URL"));
  }

  @Override
  public boolean test(final TaskType taskType) {
    return TaskType.RECORD == taskType;
  }
}
