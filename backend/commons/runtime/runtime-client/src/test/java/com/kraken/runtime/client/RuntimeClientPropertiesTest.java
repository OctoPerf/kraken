package com.kraken.runtime.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class RuntimeClientPropertiesTest {

  public static final RuntimeClientProperties RUNTIME_PROPERTIES = RuntimeClientProperties.builder()
      .runtimeUrl("runtimeUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(RUNTIME_PROPERTIES);
  }

}
