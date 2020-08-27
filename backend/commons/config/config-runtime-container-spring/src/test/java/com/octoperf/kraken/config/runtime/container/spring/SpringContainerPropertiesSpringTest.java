package com.octoperf.kraken.config.runtime.container.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SpringContainerPropertiesSpringTest {
  @Autowired
  ContainerProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getTaskId()).isNotNull();
    assertThat(properties.getName()).isNotNull();
    assertThat(properties.getHostId()).isNotNull();
    Assertions.assertThat(properties.getTaskType()).isNotNull();
    assertThat(properties.getUserId()).isNotNull();
    assertThat(properties.getApplicationId()).isNotNull();
    assertThat(properties.getProjectId()).isNotNull();
  }

}

