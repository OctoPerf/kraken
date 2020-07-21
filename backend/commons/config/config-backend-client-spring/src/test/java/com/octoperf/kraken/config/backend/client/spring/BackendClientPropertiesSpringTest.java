package com.octoperf.kraken.config.backend.client.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class BackendClientPropertiesSpringTest {
  @Autowired
  BackendClientProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getUrl()).isNotNull();
  }

}
