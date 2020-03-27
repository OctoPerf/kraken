package com.kraken.runtie.server.properties;

import com.kraken.runtime.server.properties.ImmutableRuntimeServerProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class KrakentServerPropertiesTest {

  public static final ImmutableRuntimeServerProperties RUNTIME_SERVER_PROPERTIES = ImmutableRuntimeServerProperties.builder()
      .configurationPath("tasks/docker/configuration.yaml")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_SERVER_PROPERTIES);
  }
}
