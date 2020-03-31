package com.kraken.config.telegraf.spring;

import com.kraken.Application;
import com.kraken.config.telegraf.api.TelegrafProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressFBWarnings(value = {"DMI_HARDCODED_ABSOLUTE_FILENAME"}, justification = "It's just test values")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TelegrafPropertiesSpringTest {

  @Autowired
  TelegrafProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertThat("/etc/telegraf/telegraf.conf").isEqualTo(properties.getLocal());
    assertThat("telegraf/telegraf.conf").isEqualTo(properties.getRemote());
  }
}


