package com.kraken.gatling.runner;

import com.kraken.gatling.runner.GatlingProperties;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

public class GatlingPropertiesTest {

  public static final GatlingProperties GATLING_PROPERTIES = GatlingProperties.builder()
      .gatlingHome(Path.of("gatlingHome"))
      .gatlingBin(Path.of("gatlingBin"))
      .localUserFiles(Path.of("localUserFiles"))
      .localConf(Path.of("localConf"))
      .localResult(Path.of("localResult"))
      .remoteUserFiles(Optional.of("remoteUserFiles"))
      .remoteConf(Optional.of("remoteConf"))
      .remoteResult(Optional.of("remoteResult"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }
}
