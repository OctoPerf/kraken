package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.InfluxDBClientProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
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

  @NonNull InfluxDBClientProperties properties;
  @NonNull RuntimeContainerProperties container;

  @Override
  public Command get() {
    return Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
            .put(KRAKEN_INFLUXDB_URL, properties.getUrl())
            .put(KRAKEN_INFLUXDB_DATABASE, properties.getDatabase())
            .put(KRAKEN_INFLUXDB_USER, properties.getUser())
            .put(KRAKEN_INFLUXDB_PASSWORD, properties.getPassword())
            .put(KRAKEN_TEST_ID, container.getTaskId())
            .put(KRAKEN_INJECTOR, container.getHostId())
            .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build();
  }
}
