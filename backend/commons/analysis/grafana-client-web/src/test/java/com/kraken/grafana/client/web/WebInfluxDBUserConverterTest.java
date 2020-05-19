package com.kraken.grafana.client.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static com.kraken.grafana.client.api.GrafanaUser.*;
import static com.kraken.grafana.client.api.GrafanaUserTest.GRAFANA_USER;
import static com.kraken.security.entity.user.KrakenUserTest.KRAKEN_USER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebGrafanaUserConverter.class)
public class WebInfluxDBUserConverterTest {

  @Autowired
  WebGrafanaUserConverter converter;

  @Test
  public void shouldApply() {
    final var krakenUser = KRAKEN_USER.withAttributes(ImmutableMap.of(
        USER_ID_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getId()),
        EMAIL_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getEmail()),
        PASSWORD_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getPassword()),
        DATASOURCE_NAME_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getDatasourceName()),
        ORGANIZATION_ID, ImmutableList.of(GRAFANA_USER.getOrgId())
    ));
    Assertions.assertThat(converter.apply(krakenUser)).isEqualTo(GRAFANA_USER);
  }
}