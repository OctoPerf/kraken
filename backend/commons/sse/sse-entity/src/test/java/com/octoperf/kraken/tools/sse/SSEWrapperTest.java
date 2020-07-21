package com.octoperf.kraken.tools.sse;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

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
    TestUtils.shouldPassAll(WRAPPER_STRING);
  }

}
