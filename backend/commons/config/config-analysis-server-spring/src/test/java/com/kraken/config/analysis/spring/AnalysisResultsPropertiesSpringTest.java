package com.kraken.config.analysis.spring;

import com.kraken.Application;
import com.kraken.config.grafana.api.AnalysisResultsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AnalysisResultsPropertiesSpringTest {
  @Autowired
  AnalysisResultsProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getResultPath("testId")).isEqualTo(Paths.get( "data", "testId"));
  }

}
