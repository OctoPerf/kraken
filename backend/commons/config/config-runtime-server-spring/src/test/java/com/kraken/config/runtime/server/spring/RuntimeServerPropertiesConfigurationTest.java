package com.kraken.config.runtime.server.spring;

import com.kraken.Application;
import com.kraken.config.runtime.server.api.RuntimeServerProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class RuntimeServerPropertiesConfigurationTest {
  @Autowired
  RuntimeServerProperties serverProperties;

  @Test
  public void shouldCreateProperties() {
    assertThat(serverProperties.getConfigPath()).isNotNull();
  }
}
