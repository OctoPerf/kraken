package com.kraken.runtime.command;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class CommandTest {

  public static final Command SHELL_COMMAND = Command.builder()
      .path(".")
      .environment(ImmutableMap.of("key", "value"))
      .command(Arrays.asList("java", "--version"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(SHELL_COMMAND);
  }

}
