package com.kraken.telegraf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_TELEGRAF_CONF_LOCAL;
import static com.kraken.tools.environment.KrakenEnvironmentAtValues.$KRAKEN_TELEGRAF_CONF_REMOTE;

@Slf4j
@Configuration
class TelegrafConfiguration {

  @Autowired
  @Bean
  TelegrafProperties properties(
      @Value($KRAKEN_TELEGRAF_CONF_LOCAL) final String localConf,
      @Value($KRAKEN_TELEGRAF_CONF_REMOTE) final String remoteConf
  ) {

    final var properties = TelegrafProperties.builder()
        .localConf(Paths.get(localConf))
        .remoteConf(remoteConf)
        .build();

    log.info(properties.toString());

    return properties;
  }


}

