package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Component
class RunChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireNonNull(environment.get("KRAKEN_VERSION"));
    requireNonNull(environment.get("KRAKEN_GATLING_SIMULATION"));
    requireNonNull(environment.get("KRAKEN_DESCRIPTION"));
    requireNonNull(environment.get("KRAKEN_TASK_ID"));
    requireNonNull(environment.get("KRAKEN_INFLUXDB_URL"));
    requireNonNull(environment.get("KRAKEN_INFLUXDB_DATABASE"));
    requireNonNull(environment.get("KRAKEN_INFLUXDB_USER"));
    requireNonNull(environment.get("KRAKEN_INFLUXDB_PASSWORD"));
    requireNonNull(environment.get("KRAKEN_RUNTIME_URL"));
    requireNonNull(environment.get("KRAKEN_STORAGE_URL"));
  }

  @Override
  public boolean test(final TaskType taskType) {
    return TaskType.RUN == taskType;
  }
}
