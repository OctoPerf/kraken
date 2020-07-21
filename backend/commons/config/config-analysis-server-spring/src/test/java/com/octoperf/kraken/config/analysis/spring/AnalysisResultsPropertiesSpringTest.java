package com.octoperf.kraken.config.analysis.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.grafana.api.AnalysisResultsProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class AnalysisResultsPropertiesSpringTest {
  @Autowired
  AnalysisResultsProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getResultPath("testId")).isEqualTo(Paths.get( "data", "testId"));
  }

}
