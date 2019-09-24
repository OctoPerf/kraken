package com.kraken.telegraf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Slf4j
@Configuration
class TelegrafConfiguration {

  @Autowired
  @Bean
  TelegrafProperties properties(
      @Value("${kraken.telegraf.conf.local:#{environment.KRAKEN_TELEGRAF_CONF_LOCAL}}") final String localConf,
      @Value("${kraken.telegraf.conf.remote:#{environment.KRAKEN_TELEGRAF_CONF_REMOTE}}") final String remoteConf
  ) {

    final var properties = TelegrafProperties.builder()
        .localConf(Paths.get(localConf))
        .remoteConf(remoteConf)
        .build();

    log.info(properties.toString());

    return properties;
  }


}

