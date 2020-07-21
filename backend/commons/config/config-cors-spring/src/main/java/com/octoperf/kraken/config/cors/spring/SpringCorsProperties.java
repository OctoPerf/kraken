package com.octoperf.kraken.config.cors.spring;

import com.octoperf.kraken.config.cors.api.CorsProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties(prefix = "kraken.cors")
class SpringCorsProperties implements CorsProperties {
  Boolean enabled;
  List<String> allowedOrigins;
  List<String> allowedMethods;
  List<String> allowedHeaders;
  Boolean allowCredentials;
  Duration maxAge;

  public SpringCorsProperties(Boolean enabled,
                              List<String> allowedOrigins,
                              List<String> allowedMethods,
                              List<String> allowedHeaders,
                              Boolean allowCredentials,
                              Duration maxAge) {
    this.enabled = ofNullable(enabled).orElse(false);
    this.allowedOrigins = ofNullable(allowedOrigins).orElse(of());
    this.allowedMethods = ofNullable(allowedMethods).orElse(of());
    this.allowedHeaders = ofNullable(allowedHeaders).orElse(of());
    this.allowCredentials = ofNullable(allowCredentials).orElse(false);
    this.maxAge = ofNullable(maxAge).orElse(Duration.ofDays(1));
  }
}
