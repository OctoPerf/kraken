package com.kraken.runtime.container.properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {RuntimeContainerConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class RuntimeContainerConfigurationTest {

  @Autowired
  RuntimeContainerProperties  properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getTaskId()).isNotNull();
    assertThat(properties.getContainerId()).isNotNull();
  }

}

