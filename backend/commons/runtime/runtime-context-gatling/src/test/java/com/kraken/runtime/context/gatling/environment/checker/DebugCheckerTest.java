package com.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.gatling.environment.checker.DebugChecker;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DebugChecker.class})
public class DebugCheckerTest {

  @Autowired
  DebugChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test("RUN")).isFalse();
    assertThat(checker.test("DEBUG")).isTrue();
    assertThat(checker.test("RECORD")).isFalse();
  }


  @Test(expected = NullPointerException.class)
  public void shouldFailCheck() {
    checker.accept(ImmutableMap.of());
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_GATLING_SIMULATION, "value")
        .put(KRAKEN_ANALYSIS_URL, "value")
        .put(KRAKEN_STORAGE_URL, "value")
        .build();
    checker.accept(env);
  }

  @Test
  public void shouldTestUtils(){
    TestUtils.shouldPassNPE(DebugChecker.class);
  }
}
