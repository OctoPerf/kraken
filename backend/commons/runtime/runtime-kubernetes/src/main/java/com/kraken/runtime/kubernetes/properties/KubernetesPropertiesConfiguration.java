package com.kraken.runtime.kubernetes.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KubernetesPropertiesConfiguration {

  @Autowired
  @Bean
  KubernetesProperties properties(@Value("${kraken.k8s.namespace:#{environment.KRAKEN_K8S_NAMESPACE}}") final String namespace
  ) {
    return KubernetesProperties.builder()
        .namespace(namespace)
        .build();
  }

}
