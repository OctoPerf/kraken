package com.kraken.analysis.properties.spring;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class InfluxDBPropertiesTest {

  public static final ImmutableInfluxDBProperties INFLUX_DB_CLIENT_PROPERTIES = ImmutableInfluxDBProperties.builder()
      .url("influxdbUrl")
      .user("influxdbUser")
      .password("influxdbPassword")
      .database("influxdbDatabase")
      .build();

  @Test
  public void shouldPassTestUtils() {
    INFLUX_DB_CLIENT_PROPERTIES.log();
    shouldPassAll(INFLUX_DB_CLIENT_PROPERTIES);
  }

}
