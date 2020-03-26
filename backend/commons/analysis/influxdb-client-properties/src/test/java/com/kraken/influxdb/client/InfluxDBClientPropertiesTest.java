package com.kraken.influxdb.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

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
    TestUtils.shouldPassAll(INFLUX_DB_CLIENT_PROPERTIES);
  }

}
