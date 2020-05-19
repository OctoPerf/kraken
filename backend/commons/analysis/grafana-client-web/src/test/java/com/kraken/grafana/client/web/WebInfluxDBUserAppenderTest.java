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
@SpringBootTest(classes = WebGrafanaUserAppender.class)
public class WebInfluxDBUserAppenderTest {

  @Autowired
  WebGrafanaUserAppender appender;

  @Test
  public void shouldTest() {
    Assertions.assertThat(appender.test(KRAKEN_USER)).isFalse();
    Assertions.assertThat(appender.test(KRAKEN_USER.withAttributes(ImmutableMap.of(EMAIL_ATTRIBUTE, ImmutableList.of("email"))))).isTrue();
  }

  @Test
  public void shouldApply() {
    Assertions.assertThat(appender.apply(KRAKEN_USER, GRAFANA_USER).getAttributes()).isEqualTo(
        ImmutableMap.of(
            USER_ID_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getId()),
            PASSWORD_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getPassword()),
            EMAIL_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getEmail()),
            DATASOURCE_NAME_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getDatasourceName()),
            ORGANIZATION_ID, ImmutableList.of(GRAFANA_USER.getOrgId())

        )
    );
  }

  @Test
  public void shouldApplyWithNull() {
    Assertions.assertThat(appender.apply(KRAKEN_USER.withAttributes(null), GRAFANA_USER).getAttributes()).isEqualTo(
        ImmutableMap.of(
            USER_ID_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getId()),
            PASSWORD_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getPassword()),
            EMAIL_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getEmail()),
            DATASOURCE_NAME_ATTRIBUTE, ImmutableList.of(GRAFANA_USER.getDatasourceName()),
            ORGANIZATION_ID, ImmutableList.of(GRAFANA_USER.getOrgId())
        )
    );
  }
}