package com.octoperf.kraken.grafana.client.api;

import com.octoperf.kraken.security.entity.user.KrakenUser;

import java.util.function.Function;

public interface GrafanaUserConverter extends Function<KrakenUser, GrafanaUser> {
}
