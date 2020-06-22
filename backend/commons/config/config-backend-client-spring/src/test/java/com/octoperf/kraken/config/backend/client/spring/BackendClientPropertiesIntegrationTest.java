package com.octoperf.kraken.config.backend.client.spring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class BackendClientPropertiesIntegrationTest {

  @Test
  public void shouldResolveKrakenLocal() {
    final var properties = SpringBackendClientProperties.builder()
        .url("url")
        .hostname("kraken-local")
        .build();
    Assertions.assertThat(properties.getIp()).isEqualTo("192.168.1.17");
  }

  @Test
  public void shouldResolveLocalhost() {
    final var properties = SpringBackendClientProperties.builder()
        .url("url")
        .hostname("localhost")
        .build();
    Assertions.assertThat(properties.getIp()).isEqualTo("127.0.0.1");
  }

  @Test
  public void shouldNotResolve() {
    final var properties = SpringBackendClientProperties.builder()
        .url("url")
        .build();
    Assertions.assertThat(properties.getIp()).isEqualTo("");
    Assertions.assertThat(properties.getHostname()).isEqualTo("");
  }
}
