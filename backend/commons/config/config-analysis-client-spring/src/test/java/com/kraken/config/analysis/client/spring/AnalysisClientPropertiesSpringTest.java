package com.kraken.config.analysis.client.spring;

import com.kraken.Application;
import com.kraken.config.analysis.client.api.AnalysisClientProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AnalysisClientPropertiesSpringTest {
  @Autowired
  AnalysisClientProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getUrl()).isNotNull();
  }

}
