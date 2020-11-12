package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.octoperf.kraken.license.api.LicenseService;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentChecker;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class RunChecker implements EnvironmentChecker {

  @NonNull LicenseService licenseService;

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_SIMULATION_NAME,
        KRAKEN_INFLUXDB_URL,
        KRAKEN_INFLUXDB_DATABASE,
        KRAKEN_INFLUXDB_USER,
        KRAKEN_INFLUXDB_PASSWORD);

    final var capacity = licenseService.getGatlingCapacity();
    final var customSetup = getBoolean(environment, KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP);
    checkArgument(capacity.getAllowCustomSetup() || !customSetup, "Your license does not allow to run Gatling simulations using a custom setUp inject().");
    if (!customSetup) {
      final var concurrentUsers = getLong(environment, KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS);
      final var rampUpDuration = getDuration(environment, KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION);
      final var peakDuration = getDuration(environment, KRAKEN_GATLING_SCENARIO_PEAK_DURATION);
      checkArgument(capacity.getMaxConcurrentUsers() >= concurrentUsers, String.format("Your license allows you to run %d concurrent users maximum.", capacity.getMaxConcurrentUsers()));
      checkArgument(capacity.getMaxTestDuration().compareTo(rampUpDuration.plus(peakDuration)) >= 0, String.format("Your license allows you as long as %s maximum.", capacity.getMaxTestDuration().toString()));
    }
  }

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN);
  }
}
