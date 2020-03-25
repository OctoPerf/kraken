package com.kraken.gatling.runner;

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

  @NonNull GatlingExecutionProperties properties;

  @Override
  public Command get() {
    return Command.builder()
        .path(properties.getGatlingBin().toString())
        .environment(ImmutableMap.of(
            KRAKEN_GATLING_RESULT_INFO_LOG, properties.getInfoLog().toString(),
            KRAKEN_GATLING_RESULT_DEBUG_LOG, properties.getDebugLog().toString(),
            JAVA_OPTS, properties.getJavaOpts())
        )
        .command(ImmutableList.of(
            "./gatling.sh",
            "-s", properties.getSimulation(),
            "-rd", properties.getDescription(),
            "-rf", properties.getLocalResult().toString()))
        .build();
  }
}
