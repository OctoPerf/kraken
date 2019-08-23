package com.kraken.command.client;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class CommandClientPropertiesTest {

  public static final CommandClientProperties COMMAND_PROPERTIES = CommandClientProperties.builder()
      .commandUrl("commandUrl")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(COMMAND_PROPERTIES);
  }

}
