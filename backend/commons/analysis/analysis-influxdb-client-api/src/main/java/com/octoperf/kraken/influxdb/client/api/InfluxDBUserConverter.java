package com.octoperf.kraken.influxdb.client.api;

import com.octoperf.kraken.security.entity.user.KrakenUser;

import java.util.function.Function;

public interface InfluxDBUserConverter extends Function<KrakenUser, InfluxDBUser> {
}
