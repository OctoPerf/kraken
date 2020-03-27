package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.analysis.properties.api.InfluxDBProperties;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.container.properties.ContainerProperties;
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

  @NonNull InfluxDBProperties influxDb;
  @NonNull ContainerProperties container;

  @Override
  public Command get() {
    return Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
            .put(KRAKEN_INFLUXDB_URL, influxDb.getUrl())
            .put(KRAKEN_INFLUXDB_DATABASE, influxDb.getDatabase())
            .put(KRAKEN_INFLUXDB_USER, influxDb.getUser())
            .put(KRAKEN_INFLUXDB_PASSWORD, influxDb.getPassword())
            .put(KRAKEN_TASKID, container.getTaskId())
            .put(KRAKEN_HOSTID, container.getHostId())
            .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build();
  }
}
