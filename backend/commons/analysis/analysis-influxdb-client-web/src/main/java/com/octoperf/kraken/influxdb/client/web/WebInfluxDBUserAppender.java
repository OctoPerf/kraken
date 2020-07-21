package com.octoperf.kraken.influxdb.client.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUser;
import com.octoperf.kraken.influxdb.client.api.InfluxDBUserAppender;
import com.octoperf.kraken.security.entity.user.KrakenUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
final class WebInfluxDBUserAppender implements InfluxDBUserAppender {

  @Override
  public boolean test(final KrakenUser krakenUser) {
    return krakenUser.hasAttribute(InfluxDBUser.USERNAME_ATTRIBUTE);
  }

  @Override
  public KrakenUser apply(final KrakenUser krakenUser, final InfluxDBUser user) {
    final var currentAttributes = Optional.ofNullable(krakenUser.getAttributes()).orElse(ImmutableMap.of());
    final var builder = ImmutableMap.<String, List<String>>builder()
        .putAll(currentAttributes)
        .put(InfluxDBUser.USERNAME_ATTRIBUTE, ImmutableList.of(user.getUsername()))
        .put(InfluxDBUser.PASSWORD_ATTRIBUTE, ImmutableList.of(user.getPassword()))
        .put(InfluxDBUser.DATABASE_ATTRIBUTE, ImmutableList.of(user.getDatabase()));
    return krakenUser.withAttributes(builder.build());
  }
}
