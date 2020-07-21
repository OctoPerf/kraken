package com.kraken.keycloak.event.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Disabled("Start Keycloak before running")
class KeycloakClientIntegrationTest {

  private KeycloakClient client;

  @BeforeEach
  void setUp() {
    client = new KeycloakClient("http://localhost:9080",
        "kraken",
        "kraken-api",
        "c1ab32c0-0ba7-4289-b6c9-0ea1aa5ad1d4");
  }

  @Test
  void shouldReturnAccessToken() throws IOException {
    System.out.println(client.getAccessToken());
  }
}