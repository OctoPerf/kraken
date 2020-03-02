package com.runtime.context.entity;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionContextBuilderTest {

  public static final ExecutionContextBuilder EXECUTION_CONTEXT_BUILDER = ExecutionContextBuilder.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType("RUN")
      .description("description")
      .file("file")
      .hostEnvironments(ImmutableMap.of("hostId", ImmutableMap.of("key", "value")))
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_CONTEXT_BUILDER);
  }

  @Test
  public void shouldWither() {
    final var context = EXECUTION_CONTEXT_BUILDER
        .withHostEnvironmentVariable("hostId", "foo", "bar")
        .withHostEnvironmentVariable("hostId", "foo", "bar2")
        .withHostEnvironmentVariable("hostId2", "foo", "bar")
        .withGlobalEnvironmentVariable("abc", "abc");
    assertThat(context.getHostEnvironments()).isEqualTo(
        ImmutableMap.of("hostId", ImmutableMap.of("key", "value", "foo", "bar", "abc", "abc"),
            "hostId2", ImmutableMap.of("foo", "bar", "abc", "abc")));
  }

}
