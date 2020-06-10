package com.octoperf.kraken.config.security.client.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.security.client.api.SecurityClientProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class SecurityClientPropertiesSpringTest {
  @Autowired
  SecurityClientProperties properties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(properties.getUrl()).isEqualTo("http://localhost:9080");
    assertThat(properties.getRealm()).isEqualTo("kraken");
    assertThat(properties.getWeb().getId()).isEqualTo("kraken-web");
    assertThat(properties.getWeb().getSecret()).isEqualTo("");
    assertThat(properties.getApi().getId()).isEqualTo("kraken-api");
    assertThat(properties.getApi().getSecret()).isEqualTo("api-secret");
    assertThat(properties.getContainer().getId()).isEqualTo("kraken-container");
    assertThat(properties.getContainer().getSecret()).isEqualTo("container-secret");

  }
}
