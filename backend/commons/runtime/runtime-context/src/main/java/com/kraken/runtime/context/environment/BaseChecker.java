package com.kraken.runtime.context.environment;

import com.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.kraken.runtime.entity.task.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
class BaseChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment, KRAKEN_RUNTIME_VERSION,
        KRAKEN_DESCRIPTION,
      KRAKEN_TASKID,
      KRAKEN_TASKTYPE,
        KRAKEN_EXPECTED_COUNT,
        KRAKEN_APPLICATION_ID,
      KRAKEN_CLIENT_URL,
      KRAKEN_HOSTID);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return true;
  }

}
