package com.octoperf.kraken.runtime.entity.environment;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

import static com.octoperf.kraken.runtime.entity.environment.ExecutionEnvironmentEntrySource.USER;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.KRAKEN_VERSION;

public class ExecutionEnvironmentEntryTest {

  public static final ExecutionEnvironmentEntry EXECUTION_ENVIRONMENT_ENTRY = ExecutionEnvironmentEntry.builder()
      .scope("")
      .from(USER)
      .key(KRAKEN_VERSION.name())
      .value("bar")
      .build();


  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(EXECUTION_ENVIRONMENT_ENTRY);
  }

}
