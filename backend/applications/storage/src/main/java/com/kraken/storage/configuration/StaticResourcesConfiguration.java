package com.kraken.storage.configuration;

import com.kraken.config.api.ApplicationProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@Configuration
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE)
class StaticResourcesConfiguration implements WebFluxConfigurer {

  @NonNull ApplicationProperties kraken;

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    final var pattern = "/static/**";
    final var files = "file:" + kraken.getData() + "/";
    registry
        .addResourceHandler(pattern)
        .addResourceLocations(files);
  }

}
