package com.kraken.runtime.context.gatling.environment.checker;

import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
class RunChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_SIMULATION,
        KRAKEN_INFLUXDB_URL,
        KRAKEN_INFLUXDB_DATABASE,
        KRAKEN_INFLUXDB_USER,
        KRAKEN_INFLUXDB_PASSWORD,
        KRAKEN_STORAGE_URL);
  }

  @Override
  public boolean test(final String taskType) {
    return "RUN".equals(taskType);
  }
}
