package com.kraken.config.kraken;

import com.kraken.Application;
import com.kraken.config.api.KrakenProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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