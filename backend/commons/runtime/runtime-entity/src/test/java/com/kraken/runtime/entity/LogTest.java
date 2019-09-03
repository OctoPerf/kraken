package com.kraken.runtime.entity;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class LogTest {

  public static final Log LOG = Log.builder()
      .id("id")
      .applicationId("applicationId")
      .text("text")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(LOG);
  }

}
