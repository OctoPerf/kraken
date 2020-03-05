package com.kraken.runtime.context.gatling.environment.checker;

import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
class ContainerNamesChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_CONTAINER_NAME,
        KRAKEN_GATLING_CONTAINER_LABEL,
        KRAKEN_GATLING_SIDEKICK_NAME,
        KRAKEN_GATLING_SIDEKICK_LABEL);
  }

  @Override
  public boolean test(final String taskType) {
    return accept(taskType, "RUN", "DEBUG", "RECORD");
  }
}
