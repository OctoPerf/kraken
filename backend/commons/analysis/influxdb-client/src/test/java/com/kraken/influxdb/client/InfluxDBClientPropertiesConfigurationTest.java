package com.kraken.influxdb.client;

import com.kraken.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InfluxDBClientPropertiesConfigurationTest {

  @Qualifier("webClientInfluxdb")
  @Autowired
  WebClient client;

  @Test
  public void shouldCreateWebClients() {
    assertThat(client).isNotNull();
  }
}
