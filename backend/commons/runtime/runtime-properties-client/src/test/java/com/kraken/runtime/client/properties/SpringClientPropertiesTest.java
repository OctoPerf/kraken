package com.kraken.runtime.client.properties;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.junit.Assert.assertNotNull;

public class SpringClientPropertiesTest {

  public static final SpringClientProperties RUNTIME_PROPERTIES = SpringClientProperties.builder()
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
