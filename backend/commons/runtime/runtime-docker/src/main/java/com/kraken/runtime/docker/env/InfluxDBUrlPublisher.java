package com.kraken.runtime.docker.env;

import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.InfluxDBClientProperties;
import com.kraken.runtime.entity.ExecutionContext;
import com.kraken.runtime.entity.TaskType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class InfluxDBUrlPublisher implements EnvironmentPublisher {

  @NonNull InfluxDBClientProperties properties;

  @Override
  public boolean test(final TaskType taskType) {
    return TaskType.RUN == taskType;
  }

  @Override
  public Map<String, String> apply(final ExecutionContext context) {
    return ImmutableMap.of(KRAKEN_INFLUXDB_URL, properties.getInfluxdbUrl(),
        KRAKEN_INFLUXDB_DATABASE, properties.getInfluxdbDatabase(),
        KRAKEN_INFLUXDB_USER, properties.getInfluxdbUser(),
        KRAKEN_INFLUXDB_PASSWORD, properties.getInfluxdbPassword());
  }
}
