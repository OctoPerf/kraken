package com.kraken.config.telegraf.spring;

import com.kraken.tests.utils.TestUtils;
import org.junit.Test;

public class TelegrafPropertiesTest {

  public static final SpringTelegrafProperties TELEGRAF_PROPERTIES = SpringTelegrafProperties
    .builder()
    .local("localConf")
    .remote("remoteConf")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(TELEGRAF_PROPERTIES);
  }
}
