package com.octoperf.kraken.config.backend.client.spring;

import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

class BackendClientPropertiesTest {

  public static final BackendClientProperties BACKEND_CLIENT_PROPERTIES = SpringBackendClientProperties.builder()
      .url("backendUrl")
      .hostname("localhost")
      .ip("127.0.0.1")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(BACKEND_CLIENT_PROPERTIES.getClass());
    TestUtils.shouldPassToString(BACKEND_CLIENT_PROPERTIES);
  }
}