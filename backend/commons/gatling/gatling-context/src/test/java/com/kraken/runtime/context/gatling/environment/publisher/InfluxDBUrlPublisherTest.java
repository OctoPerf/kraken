package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableList;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.influxdb.client.api.InfluxDBUserTest;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.entity.functions.api.OwnerToUserId;
import com.kraken.security.entity.user.KrakenUserTest;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry.builder;
import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.BACKEND;
import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InfluxDBUrlPublisher.class)
@EnableAutoConfiguration
public class InfluxDBUrlPublisherTest {
  @MockBean
  InfluxDBProperties properties;
  @MockBean
  InfluxDBUserConverter influxDBUserConverter;
  @MockBean
  OwnerToUserId toUserId;
  @MockBean
  SecurityAdminClient securityAdminClient;

  @Autowired
  InfluxDBUrlPublisher publisher;

  @BeforeEach
  public void setUp() {
    given(properties.getUser()).willReturn("user");
    given(properties.getPassword()).willReturn("password");
    given(properties.getDatabase()).willReturn("database");
    given(properties.getUrl()).willReturn("url");
    given(toUserId.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER.getOwner())).willReturn(Optional.of("userId"));
    given(securityAdminClient.getUser("userId")).willReturn(Mono.just(KrakenUserTest.KRAKEN_USER));
    given(influxDBUserConverter.apply(KrakenUserTest.KRAKEN_USER)).willReturn(InfluxDBUserTest.INFLUX_DB_USER);
  }

  @Test
  public void shouldTest() {
    assertThat(publisher.test(GATLING_RUN)).isTrue();
    assertThat(publisher.test(GATLING_DEBUG)).isFalse();
    assertThat(publisher.test(GATLING_RECORD)).isFalse();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER).block();
    assertThat(env).isNotNull();
    assertThat(env).isEqualTo(ImmutableList.of(
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_URL.name()).value(properties.getUrl()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_DATABASE.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getDatabase()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_USER.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getUsername()).build(),
        builder().from(BACKEND).scope("").key(KRAKEN_INFLUXDB_PASSWORD.name()).value(InfluxDBUserTest.INFLUX_DB_USER.getPassword()).build()
    ));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(InfluxDBUrlPublisher.class);
  }
}
