package com.kraken.runtime.gatling;

import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressFBWarnings(value = {"DMI_HARDCODED_ABSOLUTE_FILENAME"}, justification = "It's just test values")
@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {GatlingExecutionConfiguration.class, RuntimeContainerPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class GatlingExecutionConfigurationTest {

  @Autowired
  GatlingExecutionProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getGatlingHome()).isEqualTo(Paths.get("/home/ubuntu/softs/gatling"));
    assertThat(properties.getGatlingBin()).isEqualTo(Paths.get("/home/ubuntu/softs/gatling/bin"));
  }
}


