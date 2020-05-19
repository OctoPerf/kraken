package com.kraken.influxdb.client.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.kraken.influxdb.client.api.InfluxDBUserTest;
import com.kraken.security.entity.user.KrakenUserTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static com.kraken.influxdb.client.api.InfluxDBUser.*;
import static com.kraken.influxdb.client.api.InfluxDBUserTest.INFLUX_DB_USER;
import static com.kraken.security.entity.user.KrakenUserTest.KRAKEN_USER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebInfluxDBUserAppender.class)
public class WebInfluxDBUserAppenderTest {

  @Autowired
  InfluxDBUserAppender appender;

  @Test
  public void shouldTest() {
    Assertions.assertThat(appender.test(KRAKEN_USER)).isFalse();
    Assertions.assertThat(appender.test(KRAKEN_USER.withAttributes(ImmutableMap.of(USERNAME_ATTRIBUTE, ImmutableList.of("username"))))).isTrue();
  }

  @Test
  public void shouldApply() {
    Assertions.assertThat(appender.apply(KRAKEN_USER, INFLUX_DB_USER).getAttributes()).isEqualTo(
        ImmutableMap.of(
            USERNAME_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getUsername()),
            PASSWORD_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getPassword()),
            DATABASE_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getDatabase())
        )
    );
  }

  @Test
  public void shouldApplyWithNull() {
    Assertions.assertThat(appender.apply(KRAKEN_USER.withAttributes(null), INFLUX_DB_USER).getAttributes()).isEqualTo(
        ImmutableMap.of(
            USERNAME_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getUsername()),
            PASSWORD_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getPassword()),
            DATABASE_ATTRIBUTE, ImmutableList.of(INFLUX_DB_USER.getDatabase())
        )
    );
  }
}