package com.octoperf.kraken.tools.configuration.cors;

import com.octoperf.kraken.config.cors.api.CorsProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CorsGlobalConfiguration implements CorsConfigurationSource {

  @NonNull CorsProperties corsProperties;

  @Override
  public CorsConfiguration getCorsConfiguration(final ServerWebExchange exchange) {
    if (corsProperties.getEnabled()) {
      final var cors = new CorsConfiguration();
      cors.setAllowedOrigins(corsProperties.getAllowedOrigins());
      cors.setAllowedMethods(corsProperties.getAllowedMethods());
      cors.setAllowedHeaders(corsProperties.getAllowedHeaders());
      cors.setAllowCredentials(corsProperties.getAllowCredentials());
      cors.setMaxAge(corsProperties.getMaxAge());
      return cors;
    }
    return null;
  }
}
