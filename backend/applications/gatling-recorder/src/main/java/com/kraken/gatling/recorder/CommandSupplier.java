package com.kraken.gatling.recorder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.gatling.api.GatlingExecutionProperties;
import com.kraken.runtime.gatling.api.GatlingLog;
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
    final GatlingLog logs = gatling.getLogs();
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
        "--har-file", gatling.getHarPath().getLocal(),
        "--package", gatling.getSimulation().getPackageName(),
        "--class-name", gatling.getSimulation().getClassName()
      ))
      .build();
  }
}
