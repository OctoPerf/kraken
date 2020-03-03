package com.kraken.runtime.entity.environment;

import org.junit.Test;

import static com.kraken.test.utils.TestUtils.shouldPassAll;

public class ExecutionEnvironmentEntryTest {

  public static final ExecutionEnvironmentEntry EXECUTION_ENVIRONMENT_ENTRY = ExecutionEnvironmentEntry.builder()
      .scope("")
      .from(ExecutionEnvironmentEntrySource.USER)
      .key("foo")
      .value("bar")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_ENVIRONMENT_ENTRY);
  }

}
