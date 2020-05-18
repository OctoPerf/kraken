package com.kraken.config.security.container.spring;

import com.kraken.Application;
import com.kraken.config.security.container.api.SecurityContainerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecurityContainerPropertiesSpringTest {

  @Autowired
  SecurityContainerProperties properties;

  @Test
  public void shouldLoadProperties() {
    assertThat(properties).isNotNull();
    assertThat(properties.getAccessToken()).isEqualTo("accessToken");
    assertThat(properties.getRefreshToken()).isEqualTo("refreshToken");
    assertThat(properties.getMinValidity()).isEqualTo(60L);
    assertThat(properties.getRefreshMinValidity()).isEqualTo(300L);
    assertThat(properties.getExpiresIn()).isEqualTo(300L);
    assertThat(properties.getRefreshExpiresIn()).isEqualTo(1800L);
  }
}
