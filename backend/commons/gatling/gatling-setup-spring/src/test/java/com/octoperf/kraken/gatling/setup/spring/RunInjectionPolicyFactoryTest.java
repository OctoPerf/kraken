package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.gatling.api.GatlingScenario;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RunInjectionPolicyFactoryTest {

  @Mock
  GatlingProperties gatlingProperties;
  @Mock
  GatlingScenario setup;

  RunInjectionPolicyFactory policy;

  @BeforeEach
  public void setUp() {
    policy = new RunInjectionPolicyFactory(gatlingProperties);
  }

  @Test
  public void shouldReturnTaskType() {
    assertThat(policy.get()).isEqualTo(TaskType.GATLING_RUN);
  }

  @Test
  public void shouldReturnSimpleInjectionPolicy() {
    given(gatlingProperties.getScenario()).willReturn(setup);
    given(setup.getConcurrentUsers()).willReturn(10L);
    given(setup.getRampUpDuration()).willReturn(Duration.ofMinutes(2));
    given(setup.getPeakDuration()).willReturn(Duration.ofMinutes(1));
    assertThat(policy.apply(1)).isEqualTo(of(".inject(rampConcurrentUsers(0) to (10) during (120 seconds), constantConcurrentUsers(10) during (60 seconds))"));
  }

  @Test
  public void shouldReturnInjectionPolicy() {
    given(gatlingProperties.getScenario()).willReturn(setup);
    given(setup.getConcurrentUsers()).willReturn(10L);
    given(setup.getRampUpDuration()).willReturn(Duration.ofMinutes(2));
    given(setup.getPeakDuration()).willReturn(Duration.ofMinutes(1));
    assertThat(policy.apply(3)).isEqualTo(of(
        ".inject(rampConcurrentUsers(0) to (3) during (120 seconds), constantConcurrentUsers(3) during (60 seconds))",
        ".inject(rampConcurrentUsers(0) to (3) during (120 seconds), constantConcurrentUsers(3) during (60 seconds))",
        ".inject(rampConcurrentUsers(0) to (4) during (120 seconds), constantConcurrentUsers(4) during (60 seconds))"
    ));
  }

}