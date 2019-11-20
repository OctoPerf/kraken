package com.kraken.influxdb.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.kraken.tools.environment.KrakenEnvironmentAtValues.*;

@Slf4j
@Configuration
class InfluxDBClientPropertiesConfiguration {

  @Autowired
  @Bean
  InfluxDBClientProperties influxDBClientProperties(@Value($KRAKEN_INFLUXDB_URL) final String influxdbUrl,
                                                    @Value($KRAKEN_INFLUXDB_USER) final String influxdbUser,
                                                    @Value($KRAKEN_INFLUXDB_PASSWORD) final String influxdbPassword,
                                                    @Value($KRAKEN_INFLUXDB_DATABASE) final String influxdbDatabase) {
    log.info("InfluxDB URL is set to " + influxdbUrl);
    log.info("InfluxDB Database is set to " + influxdbDatabase);
    log.info("InfluxDB user path is set to " + influxdbUser);

    return InfluxDBClientProperties.builder()
        .influxdbUrl(influxdbUrl)
        .influxdbUser(influxdbUser)
        .influxdbPassword(influxdbPassword)
        .influxdbDatabase(influxdbDatabase)
        .build();
  }

}
