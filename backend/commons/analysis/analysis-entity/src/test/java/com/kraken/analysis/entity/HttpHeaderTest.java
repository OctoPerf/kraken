package com.kraken.analysis.entity;

import com.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class HttpHeaderTest {

  public static final HttpHeader HTTP_HEADER = HttpHeader.builder()
      .key("key")
      .value("value")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(HTTP_HEADER);
  }

}
