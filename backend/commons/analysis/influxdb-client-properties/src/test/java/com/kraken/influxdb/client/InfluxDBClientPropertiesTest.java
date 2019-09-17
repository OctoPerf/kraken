package com.kraken.influxdb.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class InfluxDBClientPropertiesTest {

  public static final InfluxDBClientProperties INFLUX_DB_CLIENT_PROPERTIES = InfluxDBClientProperties.builder()
      .influxdbUrl("influxdbUrl")
      .influxdbUser("influxdbUser")
      .influxdbPassword("influxdbPassword")
      .influxdbDatabase("influxdbDatabase")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(INFLUX_DB_CLIENT_PROPERTIES);
  }

}
