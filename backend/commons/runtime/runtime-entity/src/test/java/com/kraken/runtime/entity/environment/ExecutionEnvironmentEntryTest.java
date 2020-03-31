package com.kraken.runtime.entity.environment;

import org.junit.Test;

import static com.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static com.kraken.test.utils.TestUtils.shouldPassAll;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

public class ExecutionEnvironmentEntryTest {

  public static final ExecutionEnvironmentEntry EXECUTION_ENVIRONMENT_ENTRY = ExecutionEnvironmentEntry.builder()
      .scope("")
      .from(USER)
      .key(KRAKEN_VERSION.name())
      .value("bar")
      .build();


  @Test
  public void shouldPassTestUtils() {
    shouldPassAll(EXECUTION_ENVIRONMENT_ENTRY);
  }

}
