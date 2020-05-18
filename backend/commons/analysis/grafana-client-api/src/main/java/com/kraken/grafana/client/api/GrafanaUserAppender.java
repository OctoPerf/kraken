package com.kraken.grafana.client.api;

import com.kraken.security.entity.user.KrakenUser;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface GrafanaUserAppender extends Predicate<KrakenUser>, BiFunction<KrakenUser, GrafanaUser, KrakenUser> {
}
