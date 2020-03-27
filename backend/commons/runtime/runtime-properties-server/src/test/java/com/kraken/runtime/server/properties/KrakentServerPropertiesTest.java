package com.kraken.runtime.server.properties;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class KrakentServerPropertiesTest {

  public static final SpringServerProperties RUNTIME_SERVER_PROPERTIES = SpringServerProperties.builder()
      .configPath("tasks/docker/configuration.yaml")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_SERVER_PROPERTIES);
  }
}
