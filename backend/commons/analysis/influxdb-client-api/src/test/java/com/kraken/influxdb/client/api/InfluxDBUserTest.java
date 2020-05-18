package com.kraken.influxdb.client.api;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class InfluxDBUserTest {
  public static final InfluxDBUser INFLUX_DB_USER = InfluxDBUser.builder()
      .username("username")
      .password("password")
      .database("database")
      .build();


  @Test
  public void shouldPassEquals() {
    TestUtils.shouldPassEquals(INFLUX_DB_USER.getClass());
  }

  @Test
  public void shouldPassNPE() {
    TestUtils.shouldPassNPE(InfluxDBUser.class);
  }

  @Test
  public void shouldPassToString() {
    TestUtils.shouldPassToString(INFLUX_DB_USER);
  }

}