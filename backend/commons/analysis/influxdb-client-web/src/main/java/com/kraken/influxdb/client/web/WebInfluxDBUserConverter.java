package com.kraken.influxdb.client.web;

import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.influxdb.client.api.InfluxDBUserConverter;
import com.kraken.security.entity.user.KrakenUser;
import org.springframework.stereotype.Component;

@Component
final class WebInfluxDBUserConverter implements InfluxDBUserConverter {

  @Override
  public InfluxDBUser apply(final KrakenUser krakenUser) {
    return InfluxDBUser.builder()
        .username(krakenUser.getAttribute(InfluxDBUser.USERNAME_ATTRIBUTE))
        .password(krakenUser.getAttribute(InfluxDBUser.PASSWORD_ATTRIBUTE))
        .database(krakenUser.getAttribute(InfluxDBUser.DATABASE_ATTRIBUTE))
        .build();
  }
}
