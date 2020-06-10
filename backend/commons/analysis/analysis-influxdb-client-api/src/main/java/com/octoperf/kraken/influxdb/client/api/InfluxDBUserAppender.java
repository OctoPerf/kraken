package com.octoperf.kraken.influxdb.client.api;

import com.octoperf.kraken.security.entity.user.KrakenUser;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface InfluxDBUserAppender extends Predicate<KrakenUser>, BiFunction<KrakenUser, InfluxDBUser, KrakenUser> {
}
