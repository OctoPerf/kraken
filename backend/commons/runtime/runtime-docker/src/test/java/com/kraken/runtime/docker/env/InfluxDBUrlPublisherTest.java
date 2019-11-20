package com.kraken.runtime.docker.env;

import com.kraken.influxdb.client.InfluxDBClientPropertiesTestConfiguration;
import com.kraken.runtime.entity.ExecutionContextTest;
import com.kraken.runtime.entity.TaskType;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    assertThat(publisher.test(TaskType.RUN)).isTrue();
    assertThat(publisher.test(TaskType.DEBUG)).isFalse();
    assertThat(publisher.test(TaskType.RECORD)).isFalse();
  }

  @Test
  public void shouldGet() {
    final var env = publisher.apply(ExecutionContextTest.EXECUTION_CONTEXT);
    assertThat(env.get(KRAKEN_INFLUXDB_URL)).isNotNull();
    assertThat(env.get(KRAKEN_INFLUXDB_DATABASE)).isNotNull();
    assertThat(env.get(KRAKEN_INFLUXDB_USER)).isNotNull();
    assertThat(env.get(KRAKEN_INFLUXDB_PASSWORD)).isNotNull();
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(InfluxDBUrlPublisher.class);
  }
}
