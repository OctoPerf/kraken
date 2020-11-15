package com.octoperf.kraken.config.kubernetes.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.kubernetes.api.KubernetesProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class KubernetesPropertiesSpringTest {
  @Autowired
  KubernetesProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getNamespace()).isNotNull();
  }
}
