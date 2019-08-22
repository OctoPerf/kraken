package com.kraken.commons.gatling.properties;

import com.kraken.commons.rest.configuration.ApplicationPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GatlingPropertiesConfiguration.class, ApplicationPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GatlingPropertiesConfigurationTest {

  @Autowired
  GatlingProperties gatlingProperties;

  @Test
  public void shouldCreateProperties() {
    assertThat(gatlingProperties.getTestResultPath("testId")).isEqualTo(Paths.get("testDir", "data", "testId"));
    assertThat(gatlingProperties.getVersion()).isNotBlank();
  }

}
