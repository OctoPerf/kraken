package com.kraken.influxdb.client;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {InfluxDBClientPropertiesConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class InfluxDBClientPropertiesConfigurationTest {

  @Autowired
  InfluxDBClientProperties influxDBClientProperties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(influxDBClientProperties.getInfluxdbUrl()).isNotNull();
  }

}
