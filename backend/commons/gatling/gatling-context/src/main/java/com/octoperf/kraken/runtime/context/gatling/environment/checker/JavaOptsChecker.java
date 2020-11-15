package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.octoperf.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_GATLING_JAVA_OPTS;

@Component
final class JavaOptsChecker implements EnvironmentChecker {

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_JAVA_OPTS);
  }

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN, TaskType.GATLING_DEBUG, TaskType.GATLING_RECORD);
  }
}
