package com.kraken.gatling.log.parser;

import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GatlingParserConfiguration.class, RuntimeContainerPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GatlingParserConfigurationTest {

  @Autowired
  GatlingParserProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getDebugLog()).isNotNull();
  }
}


