package com.kraken.runtime.context.gatling.environment.publisher;

import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.task.TaskType;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry.builder;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class InfluxDBUrlPublisher implements EnvironmentPublisher {

  @NonNull InfluxDBProperties properties;

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN);
  }

  @Override
  public ExecutionContextBuilder apply(final ExecutionContextBuilder context) {
    return context.addEntries(of(
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_URL.name()).value(properties.getUrl()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_DATABASE.name()).value(properties.getDatabase()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_USER.name()).value(properties.getUser()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_PASSWORD.name()).value(properties.getPassword()).build()
    ));
  }
}
