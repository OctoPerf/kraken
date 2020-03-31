package com.kraken.config.har.parser.spring;

import com.kraken.Application;
import com.kraken.config.har.parser.api.HarParserProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class HarParserConfigurationTest {
  @Autowired
  HarParserProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getLocal()).isNotNull();
    assertThat(properties.getRemote()).isNotNull();
  }
}


