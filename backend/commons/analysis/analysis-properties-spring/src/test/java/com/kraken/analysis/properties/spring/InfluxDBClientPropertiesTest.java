package com.kraken.analysis.properties.spring;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class InfluxDBClientPropertiesTest {

  public static final ImmutableInfluxDBClientProperties INFLUX_DB_CLIENT_PROPERTIES = ImmutableInfluxDBClientProperties.builder()
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
