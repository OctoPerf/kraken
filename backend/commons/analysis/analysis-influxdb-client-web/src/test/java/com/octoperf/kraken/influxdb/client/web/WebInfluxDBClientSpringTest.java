package com.octoperf.kraken.influxdb.client.web;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClient;
import com.octoperf.kraken.influxdb.client.api.InfluxDBClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class WebInfluxDBClientSpringTest {

  @Autowired
  InfluxDBClientBuilder clientBuilder;
  @MockBean
  InfluxDBProperties properties;

  InfluxDBClient client;

  @BeforeEach
  public void setUp() {
    when(properties.getUrl()).thenReturn("http://localhost:8086");
    when(properties.getUser()).thenReturn("root");
    when(properties.getPassword()).thenReturn("admin");
    client = clientBuilder.build().block();
  }

  @Test
  public void shouldCreateWebClients() {
    assertThat(client).isNotNull();
  }
}
