package com.octoperf.kraken.config.kraken;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.api.KrakenProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class KrakenPropertiesConfigTest {

  @Autowired
  KrakenProperties properties;

  @Test
  public void shouldAutowire() {
    assertNotNull(properties);
  }
}