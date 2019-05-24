package com.kraken.analysis;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {AnalysisConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class AnalysisPropertiesConfigurationTest {

  @Autowired
  AnalysisProperties analysisProperties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(analysisProperties.getAnalysisUrl()).isNotNull();
    Assertions.assertThat(analysisProperties.getRunProperties()).isNotNull();
  }

}
