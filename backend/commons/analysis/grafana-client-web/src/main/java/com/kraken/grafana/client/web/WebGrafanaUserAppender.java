package com.kraken.grafana.client.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.grafana.client.api.GrafanaUser;
import com.kraken.grafana.client.api.GrafanaUserAppender;
import com.kraken.security.entity.user.KrakenUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
final class WebGrafanaUserAppender implements GrafanaUserAppender {

  @Override
  public boolean test(final KrakenUser krakenUser) {
    return krakenUser.hasAttribute(GrafanaUser.EMAIL_ATTRIBUTE);
  }

  @Override
  public KrakenUser apply(final KrakenUser krakenUser, final GrafanaUser user) {
    final var currentAttributes = Optional.ofNullable(krakenUser.getAttributes()).orElse(ImmutableMap.of());
    final var builder = ImmutableMap.<String, List<String>>builder()
        .putAll(currentAttributes)
        .put(GrafanaUser.USER_ID_ATTRIBUTE, ImmutableList.of(user.getId()))
        .put(GrafanaUser.EMAIL_ATTRIBUTE, ImmutableList.of(user.getEmail()))
        .put(GrafanaUser.PASSWORD_ATTRIBUTE, ImmutableList.of(user.getPassword()))
        .put(GrafanaUser.DATASOURCE_NAME_ATTRIBUTE, ImmutableList.of(user.getDatasourceName()))
        .put(GrafanaUser.ORGANIZATION_ID, ImmutableList.of(user.getOrgId()));
    return krakenUser.withAttributes(builder.build());
  }
}
