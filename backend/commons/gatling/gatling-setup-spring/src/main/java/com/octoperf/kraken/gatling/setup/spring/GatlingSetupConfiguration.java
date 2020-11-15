package com.octoperf.kraken.gatling.setup.spring;

import com.octoperf.kraken.runtime.entity.task.TaskType;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
class GatlingSetupConfiguration {

  @Bean
  public Map<TaskType, InjectionPolicyFactory> policyFactories(@NonNull final List<InjectionPolicyFactory> factories) {
    return factories.stream().collect(Collectors.toMap(InjectionPolicyFactory::get, factory -> factory));
  }
}
