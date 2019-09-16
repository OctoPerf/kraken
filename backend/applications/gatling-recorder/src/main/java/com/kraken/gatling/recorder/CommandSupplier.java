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
            "KRAKEN_GATLING_RESULT_INFO_LOG", gatlingExecutionProperties.getInfoLog().toString(),
            "KRAKEN_GATLING_RESULT_DEBUG_LOG", gatlingExecutionProperties.getDebugLog().toString())
        )
        .command(ImmutableList.of(
            "./gatling.sh",
            "--headless", "true",
            "--mode", "Har",
            "--har-file", gatlingExecutionProperties.getLocalHarPath().toString(),
            "--package", gatlingExecutionProperties.getSimulationPackage(),
            "--class-name", gatlingExecutionProperties.getSimulationClass()
        ))
        .build();
  }
}
