package com.kraken.gatling.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.gatling.GatlingExecutionProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class CommandSupplier implements Supplier<Command> {
  @NonNull GatlingExecutionProperties gatling;

  @Override
  public Command get() {
    return Command.builder()
      .path(gatling.getBin())
      .environment(ImmutableMap.of(
        KRAKEN_GATLING_INFOLOG, gatling.getInfoLog(),
        KRAKEN_GATLING_DEBUGLOG, gatling.getDebugLog(),
        JAVA_OPTS, gatling.getJavaOpts())
      )
      .command(ImmutableList.of(
        "./recorder.sh",
        "--headless", "true",
        "--mode", "Har",
        "--har-file", gatling.getLocalHarPath(),
        "--package", gatling.getSimulationPackage(),
        "--class-name", gatling.getSimulationClass()
      ))
      .build();
  }
}
