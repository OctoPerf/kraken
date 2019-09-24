package com.kraken.telegraf;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;

public class TelegrafPropertiesTest {

  public static final TelegrafProperties TELEGRAF_PROPERTIES = TelegrafProperties.builder()
      .localConf(Path.of("localConf"))
      .remoteConf("remoteConf")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(TELEGRAF_PROPERTIES);
  }
}
