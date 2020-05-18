package com.kraken.influxdb.client.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.security.entity.user.KrakenUserTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.influxdb.client.api.InfluxDBUser.*;
import static com.kraken.influxdb.client.api.InfluxDBUserTest.INFLUX_DB_USER;
import static com.kraken.security.entity.user.KrakenUserTest.KRAKEN_USER;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebInfluxDBUserConverter.class)
public class WebInfluxDBUserConverterTest {

  @Autowired
  WebInfluxDBUserConverter converter;

  @Test
  public void shouldApply() {
    final var krakenUser = KRAKEN_USER.withAttributes(ImmutableMap.of(
        USERNAME_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getUsername()),
        PASSWORD_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getPassword()),
        DATABASE_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getDatabase())
    ));
    Assertions.assertThat(converter.apply(krakenUser)).isEqualTo(INFLUX_DB_USER);
  }
}