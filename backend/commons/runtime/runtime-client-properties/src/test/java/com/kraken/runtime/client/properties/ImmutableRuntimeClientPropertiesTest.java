package com.kraken.runtime.client.properties;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.junit.Assert.assertNotNull;

public class ImmutableRuntimeClientPropertiesTest {

  public static final ImmutableRuntimeClientProperties RUNTIME_PROPERTIES = ImmutableRuntimeClientProperties.builder()
      .url("runtimeUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RUNTIME_PROPERTIES);
  }

  @Test
  public void shouldPostConstruct() {
    RUNTIME_PROPERTIES.log();
    assertNotNull(RUNTIME_PROPERTIES);
  }
}
