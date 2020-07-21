package com.octoperf.kraken.config.cors.spring;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.cors.api.CorsProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class CorsPropertiesSpringTest {
  @Autowired
  CorsProperties properties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(properties.getAllowedOrigins()).isEqualTo(ImmutableList.of("*"));
    Assertions.assertThat(properties.getAllowedMethods()).isEqualTo(ImmutableList.of("GET", "POST"));
    Assertions.assertThat(properties.getAllowedHeaders()).isEqualTo(ImmutableList.of("*"));
    Assertions.assertThat(properties.getMaxAge()).isEqualTo(Duration.ofHours(1));
    Assertions.assertThat(properties.getAllowCredentials()).isTrue();
  }
}