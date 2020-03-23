package com.kraken.storage.configuration;

import com.kraken.tools.properties.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Slf4j
@Configuration
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class StaticResourcesConfiguration implements WebFluxConfigurer {

  @NonNull ApplicationProperties applicationProperties;

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    final var pattern = "/static/**";
    final var files = "file:" + applicationProperties.getData() + "/";
    registry
        .addResourceHandler(pattern)
        .addResourceLocations(files);
  }

}
