package com.kraken.influxdb.client;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Slf4j
@Configuration
class InfluxDBClientConfiguration {

  @Autowired
  @Bean
  InfluxDBClientProperties influxDBClientProperties(@Value("${kraken.influxdb.url:#{environment.KRAKEN_INFLUXDB_URL}}") final String influxdbUrl,
                                              @Value("${kraken.influxdb.user:#{environment.KRAKEN_INFLUXDB_USER}}") final String influxdbUser,
                                              @Value("${kraken.influxdb.password:#{environment.KRAKEN_INFLUXDB_PASSWORD}}") final String influxdbPassword,
                                              @Value("${kraken.influxdb.database:#{environment.KRAKEN_INFLUXDB_DATABASE}}") final String influxdbDatabase) {
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

  @Bean("webClientInfluxdb")
  @Autowired
  WebClient influxdbWebClient(final InfluxDBClientProperties properties) {
    final var credentials = properties.getInfluxdbUser() + ":" + properties.getInfluxdbPassword();
    final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(Charsets.UTF_8));
    return WebClient
        .builder()
        .baseUrl(properties.getInfluxdbUrl())
        .defaultHeader("Authorization", "Basic " + encoded)
        .build();
  }
}
