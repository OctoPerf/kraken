package com.octoperf.kraken.runtime.command;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.octoperf.kraken.tests.utils.TestUtils.shouldPassAll;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

public class CommandTest {

  public static final Command SHELL_COMMAND = Command.builder()
      .path(".")
      .environment(ImmutableMap.of(KRAKEN_VERSION, "1.0.0"))
      .commands(Arrays.asList("java", "--version"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(SHELL_COMMAND);
  }

}
