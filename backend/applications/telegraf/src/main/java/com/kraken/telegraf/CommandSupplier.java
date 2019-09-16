package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class CommandSupplier implements Supplier<Command> {

  String simulation;
  String description;
  GatlingRunnerProperties gatlingRunnerProperties;

  @Autowired
  public CommandSupplier(
      final GatlingRunnerProperties gatlingRunnerProperties,
      @Nullable @Value("${kraken.gatling.simulation:#{environment.KRAKEN_GATLING_SIMULATION}}") final String simulation,
      @Nullable @Value("${kraken.gatling.description:#{environment.KRAKEN_GATLING_DESCRIPTION}}") final String description
  ) {
    this.gatlingRunnerProperties = Objects.requireNonNull(gatlingRunnerProperties);
    this.simulation = Optional.ofNullable(simulation).orElse("");
    this.description = Optional.ofNullable(description).orElse("");
  }

  @Override
  public Command get() {
    return Command.builder()
        .path(gatlingRunnerProperties.getGatlingBin().toString())
        .environment(ImmutableMap.of(
            "KRAKEN_GATLING_RESULT_INFO_LOG", gatlingRunnerProperties.getInfoLog().toString(),
            "KRAKEN_GATLING_RESULT_DEBUG_LOG", gatlingRunnerProperties.getDebugLog().toString())
        )
        .command(ImmutableList.of(
            "./gatling.sh",
            "-s", simulation,
            "-rd", description,
            "-rf", gatlingRunnerProperties.getLocalResult().toString()))
        .build();
  }
}
