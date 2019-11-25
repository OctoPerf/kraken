package com.kraken.gatling.log.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_GATLING_HOME;
import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_GATLING_RESULT_DEBUG_LOG;

@Slf4j
@Configuration
class GatlingParserConfiguration {

  @Autowired
  @Bean
  GatlingParserProperties gatlingProperties(
      @Value($KRAKEN_GATLING_HOME) final String home,
      @Value($KRAKEN_GATLING_RESULT_DEBUG_LOG) final String debugLog
  ) {

    final var properties = GatlingParserProperties.builder()
        .gatlingHome(Paths.get(home))
        .debugLog(Paths.get(debugLog))
        .build();

    log.info(properties.toString());

    return properties;
  }


}

