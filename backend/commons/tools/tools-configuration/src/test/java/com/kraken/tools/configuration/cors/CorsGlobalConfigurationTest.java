package com.kraken.tools.configuration.cors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.config.CorsRegistration;
import org.springframework.web.reactive.config.CorsRegistry;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CorsGlobalConfigurationTest {

  @Mock
  CorsRegistry corsRegistry;

  @Mock
  CorsRegistration corsRegistration;

  @Test
  public void shouldRegisterCorsConfiguration() {
    given(corsRegistry.addMapping("/**")).willReturn(corsRegistration);
    given(corsRegistration.allowedOrigins("*")).willReturn(corsRegistration);
    given(corsRegistration.allowedMethods("*")).willReturn(corsRegistration);
    given(corsRegistration.maxAge(3600)).willReturn(corsRegistration);

    new CorsGlobalConfiguration().addCorsMappings(corsRegistry);

    then(corsRegistration)
        .should()
        .maxAge(3600);
  }
}
