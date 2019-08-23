package com.kraken.commons.analysis.server;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class RunPropertiesTest {

  public static final RunProperties RUN_PROPERTIES = RunProperties.builder()
      .root("runnerRoot")
      .script("runnerScript")
      .cancelScript("runnerCancelScript")
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(RUN_PROPERTIES);
  }

}
