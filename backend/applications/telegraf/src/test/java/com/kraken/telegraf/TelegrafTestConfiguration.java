package com.kraken.telegraf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TelegrafTestConfiguration {

  @Bean
  TelegrafProperties telegrafProperties() {
    return TelegrafPropertiesTest.TELEGRAF_PROPERTIES;
  }

}

