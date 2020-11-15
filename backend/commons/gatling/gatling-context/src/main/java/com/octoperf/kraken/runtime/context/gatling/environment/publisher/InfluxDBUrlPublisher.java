package com.octoperf.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.octoperf.kraken.runtime.context.api.environment.EnvironmentPublisher;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class InfluxDBUrlPublisher implements EnvironmentPublisher {

  @NonNull InfluxDBProperties properties;
  @NonNull InfluxDBUserConverter influxDBUserConverter;
  @NonNull Mono<SecurityAdminClient> securityAdminClientMono;

  @Override
  public boolean test(final TaskType taskType) {
    return test(taskType, TaskType.GATLING_RUN);
  }

  @Override
  public Mono<List<ExecutionEnvironmentEntry>> apply(final ExecutionContextBuilder context) {
    return Mono.just(context.getOwner())
        .map(Owner::getUserId)
        .flatMap(userId -> securityAdminClientMono.flatMap(client -> client.getUser(userId)))
        .map(influxDBUserConverter)
        .map(user ->
            ImmutableList.of(
                ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_URL.name()).value(properties.getPublishedUrl()).build(),
                ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_DATABASE.name()).value(user.getDatabase()).build(),
                ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_USER.name()).value(user.getUsername()).build(),
                ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_PASSWORD.name()).value(user.getPassword()).build()
            ));
  }
}
