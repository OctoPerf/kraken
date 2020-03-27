package com.kraken.gatling.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.tools.properties.api.LocalRemoteProperties;
import com.kraken.gatling.properties.api.GatlingLog;
import com.kraken.gatling.properties.api.GatlingProperties;
import com.kraken.gatling.properties.api.GatlingSimulation;
import com.kraken.runtime.command.Command;
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
  @NonNull GatlingProperties gatling;

  @Override
  public Command get() {
    final GatlingLog logs = gatling.getLogs();
    final GatlingSimulation simulation = gatling.getSimulation();
    final LocalRemoteProperties harPath = gatling.getHarPath();

    return Command.builder()
      .path(gatling.getBin())
      .environment(ImmutableMap.of(
        KRAKEN_GATLING_LOGS_INFO, logs.getInfo(),
        KRAKEN_GATLING_LOGS_DEBUG, logs.getDebug(),
        JAVA_OPTS, gatling.getJavaOpts())
      )
      .command(ImmutableList.of(
        "./recorder.sh",
        "--headless", "true",
        "--mode", "Har",
        "--har-file", harPath.getLocal(),
        "--package", simulation.getPackageName(),
        "--class-name", simulation.getClassName()
      ))
      .build();
  }
}
