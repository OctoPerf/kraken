package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.InfluxDBClientProperties;
import com.kraken.runtime.command.Command;
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

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class CommandSupplier implements Supplier<Command> {

  @NonNull InfluxDBClientProperties influxDBProperties;

  @Override
  public Command get() {
    return Command.builder()
        .path(".")
        .environment(ImmutableMap.of(
            "INFLUXDB_URL", influxDBProperties.getInfluxdbUrl(),
            "INFLUXDB_DB", influxDBProperties.getInfluxdbDatabase(),
            "INFLUXDB_USER", influxDBProperties.getInfluxdbUser(),
            "INFLUXDB_USER_PASSWORD", influxDBProperties.getInfluxdbPassword()
            )
        )
        .command(ImmutableList.of("telegraf"))
        .build();
  }
}
