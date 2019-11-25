package com.kraken.har.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_GATLING_HAR_PATH_LOCAL;
import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_GATLING_HAR_PATH_REMOTE;
import static java.util.Optional.ofNullable;

@Slf4j
@Configuration
class HarParserConfiguration {

  @Autowired
  @Bean
  HarParserProperties gatlingProperties(
      @Value($KRAKEN_GATLING_HAR_PATH_LOCAL) final String localHarPath,
      @Value($KRAKEN_GATLING_HAR_PATH_REMOTE) final String remoteHarPath
  ) {

    final var properties = HarParserProperties.builder()
        .localHarPath(ofNullable(localHarPath).map(Paths::get).orElse(Path.of("")))
        .remoteHarPath(ofNullable(remoteHarPath).orElse(""))
        .build();

    log.info(properties.toString());

    return properties;
  }


}

