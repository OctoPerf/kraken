package com.octoperf.kraken.tools.configuration.cors;

import com.google.common.collect.ImmutableList;
import com.octoperf.kraken.config.cors.api.CorsProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CorsGlobalConfigurationTest {

  @Mock
  CorsProperties corsProperties;

  @Mock
  ServerWebExchange exchange;

  @Test
  void shouldGetCorsConfiguration() {
    given(corsProperties.getEnabled()).willReturn(true);
    given(corsProperties.getAllowCredentials()).willReturn(true);
    given(corsProperties.getAllowedHeaders()).willReturn(ImmutableList.of("*"));
    given(corsProperties.getAllowedMethods()).willReturn(ImmutableList.of("*"));
    given(corsProperties.getAllowedOrigins()).willReturn(ImmutableList.of("*"));
    given(corsProperties.getMaxAge()).willReturn(Duration.ofMinutes(1));

    final var config = new CorsGlobalConfiguration(corsProperties);
    final var cors = config.getCorsConfiguration(exchange);
    assertThat(cors).isNotNull();
    assertThat(cors.getAllowCredentials()).isTrue();
    assertThat(cors.getAllowedHeaders()).isEqualTo(ImmutableList.of("*"));
    assertThat(cors.getAllowedMethods()).isEqualTo(ImmutableList.of("*"));
    assertThat(cors.getAllowedOrigins()).isEqualTo(ImmutableList.of("*"));
    assertThat(cors.getMaxAge()).isEqualTo(60L);
  }

  @Test
  void shouldNotGetCorsConfiguration() {
    given(corsProperties.getEnabled()).willReturn(false);

    final var config = new CorsGlobalConfiguration(corsProperties);
    final var cors = config.getCorsConfiguration(exchange);
    assertThat(cors).isNull();
  }
}