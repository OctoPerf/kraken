package com.kraken.gatling.runner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.gatling.GatlingExecutionProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
final class CommandSupplier implements Supplier<Command> {

  @NonNull GatlingExecutionProperties gatlingExecutionProperties;

  @Override
  public Command get() {
    return Command.builder()
        .path(gatlingExecutionProperties.getGatlingBin().toString())
        .environment(ImmutableMap.of(
            KRAKEN_GATLING_RESULT_INFO_LOG, gatlingExecutionProperties.getInfoLog().toString(),
            KRAKEN_GATLING_RESULT_DEBUG_LOG, gatlingExecutionProperties.getDebugLog().toString(),
            JAVA_OPTS, gatlingExecutionProperties.getJavaOpts())
        )
        .command(ImmutableList.of(
            "./gatling.sh",
            "-s", gatlingExecutionProperties.getSimulation(),
            "-rd", gatlingExecutionProperties.getDescription(),
            "-rf", gatlingExecutionProperties.getLocalResult().toString()))
        .build();
  }
}
