package com.octoperf.kraken.runtime.context.gatling.environment.checker;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.license.api.LicenseService;
import com.octoperf.kraken.license.entity.GatlingCapacity;
import com.octoperf.kraken.license.entity.GatlingCapacityTest;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RunChecker.class})
public class RunCheckerTest {

  private final static Map<String, String> BASE_ENV = ImmutableMap.<String, String>builder()
      .put(KRAKEN_GATLING_SIMULATION_NAME.name(), "value")
      .put(KRAKEN_INFLUXDB_URL.name(), "value")
      .put(KRAKEN_INFLUXDB_DATABASE.name(), "value")
      .put(KRAKEN_INFLUXDB_USER.name(), "value")
      .put(KRAKEN_INFLUXDB_PASSWORD.name(), "value")
      .build();

  @Autowired
  RunChecker checker;

  @MockBean
  LicenseService licenseService;

  @Test
  public void shouldTest() {
    assertThat(checker.test(TaskType.GATLING_RUN)).isTrue();
    assertThat(checker.test(TaskType.GATLING_DEBUG)).isFalse();
    assertThat(checker.test(TaskType.GATLING_RECORD)).isFalse();
  }


  @Test
  public void shouldFailEmptyEnv() {
    assertThrows(NullPointerException.class, () -> checker.accept(ImmutableMap.of()));
  }

  @Test
  public void shouldFailCustomSetup() {
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "true")
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY);
    assertThrows(IllegalArgumentException.class, () -> checker.accept(env));
  }

  @Test
  public void shouldFailConcurrentUsers() {
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "false")
        .put(KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS.name(), "" + GatlingCapacityTest.GATLING_CAPACITY.getMaxConcurrentUsers() * 2)
        .put(KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION.name(), "PT0M")
        .put(KRAKEN_GATLING_SCENARIO_PEAK_DURATION.name(), "PT0M")
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY);
    assertThrows(IllegalArgumentException.class, () -> checker.accept(env));
  }

  @Test
  public void shouldFailDuration() {
    final var duration = GatlingCapacityTest.GATLING_CAPACITY.getMaxTestDuration().toString();
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "false")
        .put(KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS.name(), GatlingCapacityTest.GATLING_CAPACITY.getMaxConcurrentUsers().toString())
        .put(KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION.name(), duration)
        .put(KRAKEN_GATLING_SCENARIO_PEAK_DURATION.name(), duration)
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY);
    assertThrows(IllegalArgumentException.class, () -> checker.accept(env));
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
  public void shouldSucceedUserLoadStrategy() {
    final var duration = GatlingCapacityTest.GATLING_CAPACITY.getMaxTestDuration().dividedBy(2).toString();
    final var env = ImmutableMap.<String, String>builder()
        .putAll(BASE_ENV)
        .put(KRAKEN_GATLING_SCENARIO_CUSTOM_SETUP.name(), "false")
        .put(KRAKEN_GATLING_SCENARIO_CONCURRENT_USERS.name(), GatlingCapacityTest.GATLING_CAPACITY.getMaxConcurrentUsers().toString())
        .put(KRAKEN_GATLING_SCENARIO_RAMP_UP_DURATION.name(), duration)
        .put(KRAKEN_GATLING_SCENARIO_PEAK_DURATION.name(), duration)
        .build();
    given(licenseService.getGatlingCapacity()).willReturn(GatlingCapacityTest.GATLING_CAPACITY);
    Assertions.assertDoesNotThrow(() -> checker.accept(env));
  }

  @Test
  public void shouldTestUtils() {
    TestUtils.shouldPassNPE(RunChecker.class);
  }
}
