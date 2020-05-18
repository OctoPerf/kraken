package com.kraken.runtime.context.gatling.environment.publisher;

import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.task.TaskType;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry.builder;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class InfluxDBUrlPublisher implements EnvironmentPublisher {

  @NonNull InfluxDBProperties properties;
  @NonNull InfluxDBUserConverter influxDBUserConverter;
  @NonNull OwnerToUserId toUserId;
  @NonNull SecurityAdminClient securityAdminClient;

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN);
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(context.getOwner())
        .map(toUserId)
        .map(Optional::orElseThrow)
        .flatMap(securityAdminClient::getUser)
        .map(influxDBUserConverter)
        .map(user ->
            of(
                builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_URL.name()).value(properties.getUrl()).build(),
                builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_DATABASE.name()).value(user.getDatabase()).build(),
                builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_USER.name()).value(user.getUsername()).build(),
                builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_PASSWORD.name()).value(user.getPassword()).build()
            ));
  }
}
