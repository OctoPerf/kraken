package com.kraken.security.admin.client.keycloak;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.Application;
import com.kraken.security.admin.client.api.SecurityAdminClient;
import com.kraken.security.admin.client.api.SecurityAdminClientBuilder;
import com.kraken.security.authentication.api.AuthenticationMode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Ignore("Start keycloak before running")
public class KeycloakSecurityAdminClientIntegrationTest {

  @Autowired
  SecurityAdminClient client;

  @Test
  public void shouldGetUser() {
    final var krakenUser = client.getUser("2e44ffae-111c-4f59-ae2b-65000de6f7b7").block();
    assertThat(krakenUser).isNotNull();
    System.out.println(krakenUser);
  }

  @Test
  public void shouldSetUser() {
    final var krakenUser = client.getUser("73c523b5-6df6-4bca-9ea9-a97adfd8ef93").block();
    assertThat(krakenUser).isNotNull();
    System.out.println(krakenUser);
    final Map<String, ? extends List<String>> attributes = ImmutableMap.<String, List<String>>builder().putAll(Optional.ofNullable(krakenUser.getAttributes()).orElse(ImmutableMap.of()))
        .put("databaseUsername", ImmutableList.of("user_s1yizkoqif"))
        .put("databasePassword", ImmutableList.of("pwd_fokbs9ef0e"))
        .put("databaseName", ImmutableList.of("db_s1yizkoqif"))
        .put("dashboardUserId", ImmutableList.of("9"))
        .put("dashboardEmail", ImmutableList.of("dqs@gmail.coim"))
        .put("dashboardPassword", ImmutableList.of("hbsmkknnku"))
        .put("dashboardDatasourceName", ImmutableList.of("q2uycxeygd"))
        .build();
    final var updated = krakenUser.withAttributes(attributes);
    System.out.println(updated);
    client.setUser(krakenUser.withAttributes(attributes)).block();
  }
}