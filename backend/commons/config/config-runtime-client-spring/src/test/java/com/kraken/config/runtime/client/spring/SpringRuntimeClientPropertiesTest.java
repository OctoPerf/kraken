package com.kraken.config.runtime.client.spring;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.junit.Assert.assertNotNull;

public class SpringRuntimeClientPropertiesTest {

  public static final SpringRuntimeClientProperties RUNTIME_PROPERTIES = SpringRuntimeClientProperties.builder()
      .url("runtimeUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RUNTIME_PROPERTIES);
  }

  @Test
  public void shouldPostConstruct() {
    assertNotNull(RUNTIME_PROPERTIES);
  }
}
