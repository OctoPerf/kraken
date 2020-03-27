package com.kraken.tools.properties.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Logs All configuration within classpath.
 */
@Slf4j
@Configuration
class KrakenPropertiesConfig {

  KrakenPropertiesConfig(final List<KrakenProperties> list) {
    super();
    list.stream().map(Object::toString).forEach(log::info);
  }
}