package com.kraken.runtime.context.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.runtime.context.entity.ExecutionContextBuilder;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntry;
import com.kraken.runtime.entity.environment.ExecutionEnvironmentEntryTest;
import com.kraken.test.utils.TestUtils;
import lombok.NonNull;
import lombok.With;
import org.junit.Test;

import java.util.List;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionContextBuilderTest {

  public static final ExecutionContextBuilder EXECUTION_CONTEXT_BUILDER = ExecutionContextBuilder.builder()
      .applicationId("applicationId")
      .taskId("taskId")
      .taskType("RUN")
      .description("description")
      .file("file")
      .containersCount(42)
      .hostIds(ImmutableList.of("hostId", "other"))
      .entries(ImmutableList.of(ExecutionEnvironmentEntryTest.EXECUTION_ENVIRONMENT_ENTRY))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassEquals(EXECUTION_CONTEXT_BUILDER.getClass());
    new NullPointerTester().setDefault(ExecutionEnvironmentEntry.class, ExecutionEnvironmentEntryTest.EXECUTION_ENVIRONMENT_ENTRY).testConstructors(EXECUTION_CONTEXT_BUILDER.getClass(), PACKAGE);
    TestUtils.shouldPassToString(EXECUTION_CONTEXT_BUILDER);
  }

  @Test
  public void shouldWither() {
    final var context = EXECUTION_CONTEXT_BUILDER.addEntries(ImmutableList.of(ExecutionEnvironmentEntryTest.EXECUTION_ENVIRONMENT_ENTRY));
    assertThat(context.getEntries().size()).isEqualTo(2);
  }

}
