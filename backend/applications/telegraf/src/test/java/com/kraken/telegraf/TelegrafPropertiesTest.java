package com.kraken.telegraf;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

public class TelegrafPropertiesTest {

  public static final ImmutableTelegrafProperties TELEGRAF_PROPERTIES = ImmutableTelegrafProperties
    .builder()
    .local("localConf")
    .remote("remoteConf")
    .build();

  @Test
  public void shouldPassTestUtils() {
    TELEGRAF_PROPERTIES.log();
    TestUtils.shouldPassAll(TELEGRAF_PROPERTIES);
  }
}
