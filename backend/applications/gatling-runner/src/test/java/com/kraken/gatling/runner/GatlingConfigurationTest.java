package com.kraken.gatling.runner;

import com.kraken.runtime.container.properties.RuntimeContainerTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GatlingConfiguration.class, RuntimeContainerTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GatlingConfigurationTest {

  @Autowired
  GatlingProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getGatlingHome()).isNotNull();
  }
}


