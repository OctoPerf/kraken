package com.octoperf.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserTest;
import com.octoperf.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.security.admin.client.api.SecurityAdminClient;
import com.octoperf.kraken.security.entity.user.KrakenUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import reactor.core.publisher.Mono;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
public class InfluxDBUrlPublisherTest {
  @Mock(lenient = true)
  InfluxDBProperties properties;
  @Mock(lenient = true)
  InfluxDBUserConverter influxDBUserConverter;
  @Mock(lenient = true)
  SecurityAdminClient securityAdminClient;

  InfluxDBUrlPublisher publisher;

  @BeforeEach
  public void setUp() {
    given(properties.getUser()).willReturn("user");
    given(properties.getPassword()).willReturn("password");
    given(properties.getDatabase()).willReturn("database");
    given(properties.getPublishedUrl()).willReturn("url");
    given(securityAdminClient.getUser("userId")).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(influxDBUserConverter.apply(KrakenUserTest.KRAKEN_USER)).willReturn(InfluxDBUserTest.INFLUX_DB_USER);

    publisher = new InfluxDBUrlPublisher(properties, influxDBUserConverter, Mono.just(securityAdminClient));
  }

  @Test
  public void shouldTest() {
    assertThat(publisher.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(publisher.test(TaskType.GATLING_DEBUG)).isFalse();
    assertThat(publisher.test(TaskType.GATLING_RECORD)).isFalse();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER).block();
    assertThat(env).isNotNull();
    assertThat(env).isEqualTo(ImmutableList.of(
        ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_URL.name()).value(properties.getPublishedUrl()).build(),
        ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_DATABASE.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getDatabase()).build(),
        ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_USER.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getUsername()).build(),
        ExecutionEnvironmentEntry.builder().from(ExecutionEnvironmentEntrySource.BACKEND).scope("").key(KRAKEN_INFLUXDB_PASSWORD.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getPassword()).build()
    ));
  }

  @Test
  public void shouldTestUtils() {
    new NullPointerTester().setDefault(Mono.class, Mono.empty()).testConstructors(InfluxDBUrlPublisher.class, PACKAGE);
  }
}
