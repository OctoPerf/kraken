package com.kraken.commons.command.entity;

import com.google.common.testing.NullPointerTester;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;

public class CommandLogTest {

  public static final CommandLog SHELL_LOGS = CommandLog.builder()
      .command(CommandTest.SHELL_COMMAND)
      .text("text")
      .status(CommandLogStatus.RUNNING)
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(CommandLog.class);
    TestUtils.shouldPassToString(SHELL_LOGS);
    new NullPointerTester().setDefault(Command.class, CommandTest.SHELL_COMMAND).testConstructors(CommandLog.class, PACKAGE);
  }
}
