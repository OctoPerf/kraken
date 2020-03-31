package com.kraken.config.kraken;

import com.kraken.config.api.KrakenProperties;
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