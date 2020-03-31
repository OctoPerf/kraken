package com.kraken.config.influxdb.spring;

import com.kraken.config.influxdb.api.InfluxDBProperties;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class InfluxDBPropertiesTest {

  public static final InfluxDBProperties INFLUX_DB_CLIENT_PROPERTIES = SpringInfluxDBProperties.builder()
      .url("influxdbUrl")
      .user("influxdbUser")
      .password("influxdbPassword")
      .database("influxdbDatabase")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(INFLUX_DB_CLIENT_PROPERTIES);
  }

}
