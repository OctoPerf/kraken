package com.octoperf.kraken.config.telegraf.spring;

import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;

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
