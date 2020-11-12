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
final class DebugChecker implements EnvironmentChecker {

  @NonNull LicenseService licenseService;

  @Override
  public void accept(final Map<String, String> environment) {
    requireEnv(environment,
        KRAKEN_GATLING_SIMULATION_NAME);

    final var capacity = licenseService.getGatlingCapacity();
    final var customSetup = getBoolean(environment, KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP);
    checkArgument(capacity.getAllowCustomSetup() || !customSetup, "Your license does not allow to debug Gatling simulations using a custom setUp inject().");
  }

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_DEBUG);
  }
}
