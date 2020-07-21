package com.octoperf.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class CommandSupplier implements Supplier<Command> {

  @NonNull InfluxDBProperties influxDb;
  @NonNull ContainerProperties container;
  @NonNull ApplicationProperties properties;

  @Override
  public Command get() {
    return Command.builder()
        .path(properties.getData())
        .environment(ImmutableMap.<KrakenEnvironmentKeys, String>builder()
            .put(KRAKEN_INFLUXDB_URL, influxDb.getUrl())
            .put(KRAKEN_INFLUXDB_DATABASE, influxDb.getDatabase())
            .put(KRAKEN_INFLUXDB_USER, influxDb.getUser())
            .put(KRAKEN_INFLUXDB_PASSWORD, influxDb.getPassword())
            .put(KRAKEN_TASK_ID, container.getTaskId())
            .put(KRAKEN_HOST_ID, container.getHostId())
            .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build();
  }
}
