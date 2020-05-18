package com.kraken.config.runtime.container.spring;

import com.kraken.Application;
import com.kraken.config.runtime.container.api.ContainerProperties;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringContainerPropertiesSpringTest {
  @Autowired
  ContainerProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getTaskId()).isNotNull();
    assertThat(properties.getName()).isNotNull();
    assertThat(properties.getHostId()).isNotNull();
    assertThat(properties.getTaskType()).isNotNull();
    assertThat(properties.getUserId()).isNotNull();
    assertThat(properties.getApplicationId()).isNotNull();
  }

}

