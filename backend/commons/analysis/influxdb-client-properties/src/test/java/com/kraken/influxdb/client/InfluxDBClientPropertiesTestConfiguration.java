package com.kraken.influxdb.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class InfluxDBClientPropertiesTestConfiguration {

  @Bean
  InfluxDBClientProperties properties() {
    return InfluxDBClientPropertiesTest.INFLUX_DB_CLIENT_PROPERTIES;
  }

}
