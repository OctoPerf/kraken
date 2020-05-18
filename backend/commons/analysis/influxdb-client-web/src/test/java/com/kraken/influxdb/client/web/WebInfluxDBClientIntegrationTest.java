package com.kraken.influxdb.client.web;

import com.kraken.Application;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.influxdb.client.api.InfluxDBClient;
import com.kraken.influxdb.client.api.InfluxDBUser;
import com.kraken.tools.unique.id.IdGenerator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@Ignore("Start a dev InfluxDB")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class WebInfluxDBClientIntegrationTest {

  private InfluxDBClient client;

  @MockBean
  IdGenerator idGenerator;
  @MockBean
  InfluxDBProperties properties;

  @Before
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
