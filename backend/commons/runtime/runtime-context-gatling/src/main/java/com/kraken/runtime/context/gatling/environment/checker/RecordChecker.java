package com.kraken.runtime.context.gatling.environment.checker;

import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
class RecordChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_SIMULATION_CLASS,
        KRAKEN_GATLING_SIMULATION_PACKAGE,
        KRAKEN_GATLING_HAR_PATH_REMOTE,
        KRAKEN_ANALYSIS_URL,
        KRAKEN_STORAGE_URL);
  }

  @Override
  public boolean test(final String taskType) {
    return "RECORD".equals(taskType);
  }
}
