package com.kraken.tools.properties;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {ApplicationPropertiesConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class ApplicationPropertiesConfigurationTest {

  @Autowired
  ApplicationProperties applicationProperties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(applicationProperties.getData()).isNotNull();
  }
}
