package com.octoperf.kraken.config.telegraf.spring;

import com.octoperf.kraken.Application;
import com.octoperf.kraken.config.telegraf.api.TelegrafProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SuppressFBWarnings(value = {"DMI_HARDCODED_ABSOLUTE_FILENAME"}, justification = "It's just test values")
@ExtendWith(SpringExtension.class)
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


