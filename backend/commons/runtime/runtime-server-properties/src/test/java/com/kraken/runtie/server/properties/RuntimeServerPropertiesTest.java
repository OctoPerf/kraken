package com.kraken.runtie.server.properties;

import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class RuntimeServerPropertiesTest {

  public static final RuntimeServerProperties RUNTIME_SERVER_PROPERTIES = RuntimeServerProperties.builder()
      .configurationPath("tasks/docker/configuration.yaml")
      .version("1.3.0")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_SERVER_PROPERTIES);
  }
}
