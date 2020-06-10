package com.octoperf.kraken.runtime.context.environment;

import com.octoperf.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
final class BaseChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment, KRAKEN_VERSION,
        KRAKEN_DESCRIPTION,
        KRAKEN_TASK_ID,
        KRAKEN_TASK_TYPE,
        KRAKEN_EXPECTED_COUNT,
        KRAKEN_APPLICATION_ID,
        KRAKEN_RUNTIME_URL,
        KRAKEN_HOST_ID);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

}
