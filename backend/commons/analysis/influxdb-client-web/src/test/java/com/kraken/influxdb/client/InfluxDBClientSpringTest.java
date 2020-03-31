package com.kraken.influxdb.client;

import com.kraken.Application;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InfluxDBClientSpringTest {

  @Autowired
  InfluxDBClient client;
  @MockBean
  InfluxDBProperties properties;

  @Before
  public void setUp() {
    when(properties.getUrl()).thenReturn("http://localhost:8086");
    when(properties.getDatabase()).thenReturn("influxdbDatabase");
    when(properties.getUser()).thenReturn("root");
    when(properties.getPassword()).thenReturn("admin");
  }

  @Test
  public void shouldCreateWebClients() {
    assertThat(client).isNotNull();
  }
}
