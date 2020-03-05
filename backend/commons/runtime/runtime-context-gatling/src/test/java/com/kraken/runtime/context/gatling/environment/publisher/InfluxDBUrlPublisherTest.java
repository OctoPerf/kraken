package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableSet;
import com.kraken.influxdb.client.InfluxDBClientPropertiesTestConfiguration;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.context.gatling.environment.publisher.InfluxDBUrlPublisher;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {InfluxDBUrlPublisher.class, InfluxDBClientPropertiesTestConfiguration.class})
@EnableAutoConfiguration
public class InfluxDBUrlPublisherTest {

  @Autowired
  InfluxDBUrlPublisher publisher;

  @Test
  public void shouldTest() {
    assertThat(publisher.test("RUN")).isTrue();
    assertThat(publisher.test("DEBUG")).isFalse();
    assertThat(publisher.test("RECORD")).isFalse();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries()
        .stream()
        .map(ExecutionEnvironmentEntry::getKey)
        .collect(Collectors.toUnmodifiableSet())).isEqualTo(ImmutableSet.of(
        KRAKEN_INFLUXDB_URL, KRAKEN_INFLUXDB_DATABASE, KRAKEN_INFLUXDB_USER, KRAKEN_INFLUXDB_PASSWORD, "foo"
    ));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(InfluxDBUrlPublisher.class);
  }
}
