package com.kraken.commons.command.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class CommandClientPropertiesTest {

  public static final CommandClientProperties ANALYSIS_PROPERTIES = CommandClientProperties.builder()
      .commandUrl("commandUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(ANALYSIS_PROPERTIES);
  }

}
