package com.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.runtime.command.Command;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class CommandSupplier implements Supplier<Command> {
  @NonNull GatlingProperties gatling;

  @Override
  public Command get() {
    return Command.builder()
        .path(gatling.getBin())
        .environment(ImmutableMap.of(
          KRAKEN_GATLING_LOGS_INFO, gatling.getLogs().getInfo(),
          KRAKEN_GATLING_LOGS_DEBUG, gatling.getLogs().getDebug(),
            JAVA_OPTS, gatling.getJavaOpts())
        )
        .command(ImmutableList.of(
            "./gatling.sh",
            "-s", gatling.getSimulation().getName(),
            "-rd", gatling.getDescription(),
            "-rf", gatling.getResults().getLocal()))
        .build();
  }
}
