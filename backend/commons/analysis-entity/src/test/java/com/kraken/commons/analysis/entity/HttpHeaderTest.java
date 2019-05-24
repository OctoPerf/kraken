package com.kraken.commons.analysis.entity;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

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
