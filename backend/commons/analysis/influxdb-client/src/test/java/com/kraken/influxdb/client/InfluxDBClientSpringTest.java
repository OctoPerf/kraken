package com.kraken.influxdb.client;

import com.kraken.Application;
import com.kraken.analysis.properties.api.InfluxDBClientProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InfluxDBClientSpringTest {

  @Qualifier("webClientInfluxdb")
  @Autowired
  InfluxDBClient client;
  @Mock
  InfluxDBClientProperties properties;

  @Test
  public void shouldCreateWebClients() {
    assertThat(properties).isNotNull();
    assertThat(client).isNotNull();
  }
}
