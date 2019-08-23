package com.kraken.influxdb.client;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {InfluxDBClientConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class InfluxDBClientPropertiesConfigurationTest {

  @Autowired
  InfluxDBClientProperties influxDBClientProperties;

  @Qualifier("webClientInfluxdb")
  @Autowired
  WebClient influxdbWebClient;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(influxDBClientProperties.getInfluxdbUrl()).isNotNull();
  }

  @Test
  public void shouldCreateWebClients() {
    Assertions.assertThat(influxdbWebClient).isNotNull();
  }
}
