package com.octoperf.kraken.gatling.setup.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

class DebugInjectionPolicyFactoryTest {

  DebugInjectionPolicyFactory policy;

  @BeforeEach
  public void setUp() {
    policy = new DebugInjectionPolicyFactory();
  }

  @Test
  public void shouldReturnTaskType() {
    assertThat(policy.get()).isEqualTo(TaskType.GATLING_DEBUG);
  }

  @Test
  public void shouldReturnInjectionPolicy() {
    assertThat(policy.apply(3)).isEqualTo(of(".inject(atOnceUsers(1))", ".inject(atOnceUsers(1))", ".inject(atOnceUsers(1))"));
  }

}