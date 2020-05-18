package com.kraken.config.runtime.server.spring;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class RuntimeServerPropertiesTest {

  public static final SpringServerProperties RUNTIME_SERVER_PROPERTIES = SpringServerProperties.builder()
      .configPath("tasks/docker/configuration.yaml")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_SERVER_PROPERTIES);
  }
}
