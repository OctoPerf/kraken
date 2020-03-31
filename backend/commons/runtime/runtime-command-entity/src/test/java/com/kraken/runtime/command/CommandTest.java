package com.kraken.runtime.command;

import com.google.common.collect.ImmutableMap;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import org.junit.Test;

import java.util.Arrays;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

public class CommandTest {

  public static final Command SHELL_COMMAND = Command.builder()
      .path(".")
      .environment(ImmutableMap.of(KRAKEN_VERSION, "1.0.0"))
      .command(Arrays.asList("java", "--version"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(SHELL_COMMAND);
  }

}
