package com.kraken.config.kubernetes.spring;

import com.kraken.Application;
import com.kraken.config.kubernetes.api.KubernetesProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class KubernetesPropertiesSpringTest {
  @Autowired
  KubernetesProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getNamespace()).isNotNull();
  }
}
