package com.kraken.tools.configuration.cors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.config.CorsRegistration;
import org.springframework.web.reactive.config.CorsRegistry;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
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
