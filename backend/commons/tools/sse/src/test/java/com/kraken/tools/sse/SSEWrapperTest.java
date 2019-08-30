package com.kraken.tools.sse;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class SSEWrapperTest {

  public static final SSEWrapper<String> WRAPPER_STRING = SSEWrapper.<String>builder()
      .type("String")
      .value("value")
      .build();

  public static final SSEWrapper<Integer> WRAPPER_INT = SSEWrapper.<Integer>builder()
      .type("Integer")
      .value(42)
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(WRAPPER_STRING);
  }

}
