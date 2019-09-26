package com.kraken.runtime.kubernetes.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KubernetesPropertiesTestConfiguration {

  @Bean
  KubernetesProperties properties() {
    return KubernetesPropertiesTest.K8S_PROPERTIES;
  }

}
