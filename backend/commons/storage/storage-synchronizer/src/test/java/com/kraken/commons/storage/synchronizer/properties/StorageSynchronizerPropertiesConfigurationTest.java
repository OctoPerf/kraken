package com.kraken.commons.storage.synchronizer.properties;

import com.kraken.commons.rest.configuration.ApplicationPropertiesTestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {StorageSynchronizerPropertiesConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class StorageSynchronizerPropertiesConfigurationTest {

  @Autowired
  StorageSynchronizerProperties properties;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(properties.getUpdateFilter()).isEqualTo("test");
  }

}
