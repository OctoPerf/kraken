package com.kraken.runtime.docker.env;

import com.kraken.runtime.entity.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
class RunCheck implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    Objects.requireNonNull(environment.get("KRAKEN_VERSION"));
    Objects.requireNonNull(environment.get("KRAKEN_GATLING_SIMULATION"));
    Objects.requireNonNull(environment.get("KRAKEN_DESCRIPTION"));
    Objects.requireNonNull(environment.get("KRAKEN_TASK_ID"));
    Objects.requireNonNull(environment.get("KRAKEN_INFLUXDB_URL"));
    Objects.requireNonNull(environment.get("KRAKEN_INFLUXDB_DATABASE"));
    Objects.requireNonNull(environment.get("KRAKEN_INFLUXDB_USER"));
    Objects.requireNonNull(environment.get("KRAKEN_INFLUXDB_PASSWORD"));
    Objects.requireNonNull(environment.get("KRAKEN_RUNTIME_URL"));
    Objects.requireNonNull(environment.get("KRAKEN_STORAGE_URL"));
  }

  @Override
  public boolean test(final TaskType taskType) {
    return TaskType.RUN == taskType;
  }
}
