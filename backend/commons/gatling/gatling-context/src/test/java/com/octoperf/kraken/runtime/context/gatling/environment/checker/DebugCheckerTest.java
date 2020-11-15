package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.license.api.LicenseService;
import com.octoperf.kraken.license.entity.GatlingCapacityTest;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Map;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DebugChecker.class})
public class DebugCheckerTest {

  private final static Map<String, String> BASE_ENV = ImmutableMap.<String, String>builder()
      .put(KRAKEN_GATLING_SIMULATION_NAME.name(), "value")
      .build();

  @Autowired
  DebugChecker checker;

  @MockBean
  LicenseService licenseService;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isFalse();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isTrue();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isFalse();
  }

  @Test
  public void shouldFailEmptyEnv() {
    Assertions.assertThrows(NullPointerException.class, () -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldFailCustomSetup() {
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "true")
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY);
    Assertions.assertThrows(IllegalArgumentException.class, () -> checker.accept(env));
  }

  @Test
  public void shouldSucceedCustomSetup() {
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "true")
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY.toBuilder().allowCustomSetup(true).build());
    Assertions.assertDoesNotThrow(() -> checker.accept(env));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(DebugChecker.class);
  }
}
