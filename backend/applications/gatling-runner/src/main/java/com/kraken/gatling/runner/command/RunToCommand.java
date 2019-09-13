package com.kraken.gatling.runner.command;

import com.google.common.collect.ImmutableList;
import com.kraken.gatling.runner.GatlingProperties;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class RunToCommand implements TaskTypeToCommand {

  String simulation;
  String description;
  GatlingProperties gatlingProperties;

  @Autowired
  public RunToCommand(
      final GatlingProperties gatlingProperties,
      @Nullable @Value("${kraken.gatling.simulation:#{environment.KRAKEN_GATLING_SIMULATION}}") final String simulation,
      @Nullable @Value("${kraken.gatling.description:#{environment.KRAKEN_GATLING_DESCRIPTION}}") final String description
  ) {
    this.gatlingProperties = Objects.requireNonNull(gatlingProperties);
    this.simulation = Optional.ofNullable(simulation).orElse("");
    this.description = Optional.ofNullable(description).orElse("");
  }

  @Override
  public boolean test(TaskType taskType) {
    return taskType.equals(TaskType.RUN) || taskType.equals(TaskType.DEBUG);
  }

  @Override
  public List<String> get() {
    return ImmutableList.of(
        "./gatling.sh",
        "-s", simulation,
        "-rd", description,
        "-rf", gatlingProperties.getLocalResult().toString()
    );
  }
}
