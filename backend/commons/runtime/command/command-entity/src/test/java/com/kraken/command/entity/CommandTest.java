package com.kraken.command.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandTest {

  public static final Command SHELL_COMMAND = Command.builder()
      .id("id")
      .applicationId("app")
      .path(".")
      .environment(ImmutableMap.of("key", "value"))
      .command(Arrays.asList("java", "--version"))
      .onCancel(ImmutableList.of())
      .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(SHELL_COMMAND);
  }

  @Test
  public void shouldWither() {
    assertThat(SHELL_COMMAND.withId("otherId").getId()).isEqualTo("otherId");
    assertThat(SHELL_COMMAND.withEnvironment(ImmutableMap.of()).getEnvironment()).isEqualTo(ImmutableMap.of());
  }

}
