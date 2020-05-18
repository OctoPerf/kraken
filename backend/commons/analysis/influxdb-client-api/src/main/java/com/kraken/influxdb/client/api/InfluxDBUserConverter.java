package com.kraken.influxdb.client.api;

import com.kraken.security.entity.user.KrakenUser;

import java.util.function.Function;

public interface InfluxDBUserConverter extends Function<KrakenUser, InfluxDBUser> {
}
