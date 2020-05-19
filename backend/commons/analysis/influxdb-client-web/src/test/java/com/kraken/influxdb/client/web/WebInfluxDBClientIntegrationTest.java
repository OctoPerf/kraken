package com.kraken.influxdb.client.web;

import com.kraken.Application;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.tools.unique.id.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

// Start a dev InfluxDB
@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class WebInfluxDBClientIntegrationTest {

  private InfluxDBClient client;

  @MockBean
  IdGenerator idGenerator;
  @MockBean
  InfluxDBProperties properties;

  @BeforeEach
  public void before() {
    given(properties.getUrl()).willReturn("http://localhost:8086/");
    given(properties.getUser()).willReturn("admin");
    given(properties.getPassword()).willReturn("kraken");
    given(idGenerator.generate()).willReturn("gpe", "password");
    client = new WebInfluxDBClient(properties, idGenerator);
  }

  @Test
  public void shouldCreateUserDB() {
    System.out.println("======================>");
    System.out.println(client.createUser("userId").block());
    System.out.println("<======================");
  }

  @Test
  public void shouldDropUserDB() {
    client.deleteUser("userId").block();
  }
}
