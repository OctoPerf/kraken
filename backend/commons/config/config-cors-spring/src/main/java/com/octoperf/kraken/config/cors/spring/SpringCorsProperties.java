package com.octoperf.kraken.config.cors.spring;

import com.octoperf.kraken.config.cors.api.CorsProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;
import java.util.List;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties(prefix = "kraken.cors")
class SpringCorsProperties implements CorsProperties {
  @NonNull List<String> allowedOrigins;
  @NonNull List<String> allowedMethods;
  @NonNull List<String> allowedHeaders;
  @NonNull Boolean allowCredentials;
  @NonNull Duration maxAge;
}
