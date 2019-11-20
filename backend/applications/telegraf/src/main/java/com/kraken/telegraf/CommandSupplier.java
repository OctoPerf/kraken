package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.InfluxDBClientProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class CommandSupplier implements Supplier<Command> {

  @NonNull InfluxDBClientProperties influxDBProperties;
  @NonNull RuntimeContainerProperties containerProperties;

  @Override
  public Command get() {
    return Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
            .put(KRAKEN_INFLUXDB_URL, influxDBProperties.getInfluxdbUrl())
            .put(KRAKEN_INFLUXDB_DATABASE, influxDBProperties.getInfluxdbDatabase())
            .put(KRAKEN_INFLUXDB_USER, influxDBProperties.getInfluxdbUser())
            .put(KRAKEN_INFLUXDB_PASSWORD, influxDBProperties.getInfluxdbPassword())
            .put(KRAKEN_TEST_ID, containerProperties.getTaskId())
            .put(KRAKEN_INJECTOR, containerProperties.getHostId())
            .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build();
  }
}
