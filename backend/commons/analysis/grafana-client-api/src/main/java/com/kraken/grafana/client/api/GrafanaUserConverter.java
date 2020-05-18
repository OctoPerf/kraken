package com.kraken.grafana.client.api;

import com.kraken.security.entity.user.KrakenUser;

import java.util.function.Function;

public interface GrafanaUserConverter extends Function<KrakenUser, GrafanaUser> {
}
