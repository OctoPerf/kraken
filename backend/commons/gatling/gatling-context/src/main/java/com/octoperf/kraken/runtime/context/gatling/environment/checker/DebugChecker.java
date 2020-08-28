package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.octoperf.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_GATLING_SIMULATION_NAME;

@Component
final class DebugChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_SIMULATION_NAME);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_DEBUG);
  }
}
