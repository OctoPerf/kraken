package com.kraken.runtie.server.properties;

import com.kraken.runtime.server.properties.RuntimeServerProperties;
import com.kraken.runtime.server.properties.RuntimeServerPropertiesConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {RuntimeServerPropertiesConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class RuntimeServerPropertiesConfigurationTest {

  @Autowired
  RuntimeServerProperties runtimeServerProperties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(runtimeServerProperties.getVersion()).isNotNull();
  }
}
