package com.kraken.telegraf;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SuppressFBWarnings(value = {"DMI_HARDCODED_ABSOLUTE_FILENAME"}, justification = "It's just test values")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestPropertiesConfig.class)
public class TelegrafPropertiesSpringTest {

  @Autowired
  TelegrafProperties properties;

  @Test
  public void shouldCreateProperties() {
    assertEquals("/etc/telegraf/telegraf.conf", properties.getLocal());
    assertEquals("telegraf/telegraf.conf", properties.getRemote());
  }
}


