package com.kraken.telegraf;

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
    classes = {TelegrafConfiguration.class, RuntimeContainerPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class TelegrafConfigurationTest {

  @Autowired
  TelegrafProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat(properties.getLocalConf()).isEqualTo(Paths.get("/etc/telegraf/telegraf.conf"));
    assertThat(properties.getRemoteConf()).isEqualTo("telegraf/telegraf.conf");
  }
}


