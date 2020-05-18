package com.kraken.tools.sse;

import org.junit.Test;

import static com.kraken.tests.utils.TestUtils.shouldPassAll;

public class SSEWrapperTest {

  public static final SSEWrapper WRAPPER_STRING = SSEWrapper.builder()
      .type("String")
      .value("value")
      .build();

  public static final SSEWrapper WRAPPER_INT = SSEWrapper.builder()
      .type("Integer")
      .value(42)
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(WRAPPER_STRING);
  }

}
