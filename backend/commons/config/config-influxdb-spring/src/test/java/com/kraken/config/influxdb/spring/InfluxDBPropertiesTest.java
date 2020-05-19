package com.kraken.config.influxdb.spring;

import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class InfluxDBPropertiesTest {

  public static final InfluxDBProperties INFLUX_DB_CLIENT_PROPERTIES = SpringInfluxDBProperties.builder()
      .url("influxdbUrl")
      .user("influxdbUser")
      .password("influxdbPassword")
      .database("database")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(INFLUX_DB_CLIENT_PROPERTIES.getClass());
    TestUtils.shouldPassToString(INFLUX_DB_CLIENT_PROPERTIES);
  }

}
