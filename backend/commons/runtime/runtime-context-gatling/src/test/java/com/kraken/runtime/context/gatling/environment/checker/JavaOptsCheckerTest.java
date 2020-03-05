package com.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.context.gatling.environment.checker.JavaOptsChecker;
import com.kraken.runtime.context.gatling.environment.checker.RunChecker;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JavaOptsChecker.class})
public class JavaOptsCheckerTest {

  @Autowired
  JavaOptsChecker checker;

  @Test
  public void shouldTest() {
    assertThat(checker.test("RUN")).isTrue();
    assertThat(checker.test("DEBUG")).isTrue();
    assertThat(checker.test("RECORD")).isTrue();
  }


  @Test(expected = NullPointerException.class)
  public void shouldFailCheck() {
    checker.accept(ImmutableMap.of());
  }

  @Test
  public void shouldSucceed() {
    final var env = ImmutableMap.<String, String>builder()
        .put(KRAKEN_GATLING_JAVA_OPTS, "value")
        .build();
    checker.accept(env);
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(JavaOptsChecker.class);
  }
}
