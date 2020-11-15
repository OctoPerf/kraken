package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.runtime.entity.task.TaskType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GatlingSetupConfiguration.class, DebugInjectionPolicyFactory.class})
class GatlingScenarioConfigurationTest {

  @Autowired
  Map<TaskType, InjectionPolicyFactory> policyFactories;

  @Test
  public void shouldInjectFactories() {
    assertThat(policyFactories).isNotNull().isNotEmpty();
  }

}