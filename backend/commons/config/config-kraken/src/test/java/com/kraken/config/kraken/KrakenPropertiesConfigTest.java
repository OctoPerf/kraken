package com.kraken.config.kraken;

import com.kraken.Application;
import com.kraken.config.api.KrakenProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class KrakenPropertiesConfigTest {

  @Autowired
  KrakenProperties properties;

  @Test
  public void shouldAutowire() {
    assertNotNull(properties);
  }
}