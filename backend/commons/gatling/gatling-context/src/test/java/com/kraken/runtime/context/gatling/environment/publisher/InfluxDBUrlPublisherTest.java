package com.kraken.runtime.context.gatling.environment.publisher;

import com.google.common.collect.ImmutableSet;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.runtime.context.entity.ExecutionContextBuilderTest;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.test.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static com.kraken.runtime.entity.task.TaskType.*;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = InfluxDBUrlPublisher.class)
@EnableAutoConfiguration
public class InfluxDBUrlPublisherTest {
  @MockBean
  InfluxDBProperties properties;
  @Autowired
  InfluxDBUrlPublisher publisher;

  @Before
  public void setUp() {
    when(properties.getUser()).thenReturn("user");
    when(properties.getPassword()).thenReturn("password");
    when(properties.getDatabase()).thenReturn("database");
    when(properties.getUrl()).thenReturn("url");
  }

  @Test
  public void shouldTest() {
    assertThat(publisher.test(GATLING_RUN)).isTrue();
    assertThat(publisher.test(GATLING_DEBUG)).isFalse();
    assertThat(publisher.test(GATLING_RECORD)).isFalse();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextBuilderTest.EXECUTION_CONTEXT_BUILDER);
    assertThat(env.getEntries()
        .stream()
        .map(ExecutionEnvironmentEntry::getKey)
        .collect(Collectors.toUnmodifiableSet())).isEqualTo(ImmutableSet.of(
        KRAKEN_INFLUXDB_URL.name(), KRAKEN_INFLUXDB_DATABASE.name(), KRAKEN_INFLUXDB_USER.name(), KRAKEN_INFLUXDB_PASSWORD.name(), KRAKEN_VERSION.name()
    ));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(InfluxDBUrlPublisher.class);
  }
}
