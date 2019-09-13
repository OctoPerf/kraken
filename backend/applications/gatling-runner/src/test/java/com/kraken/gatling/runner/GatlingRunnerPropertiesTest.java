package com.kraken.gatling.runner;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

public class GatlingRunnerPropertiesTest {

  public static final GatlingRunnerProperties GATLING_PROPERTIES = GatlingRunnerProperties.builder()
      .gatlingHome(Path.of("gatlingHome"))
      .gatlingBin(Path.of("gatlingBin"))
      .localUserFiles(Path.of("localUserFiles"))
      .localConf(Path.of("localConf"))
      .localResult(Path.of("localResult"))
      .remoteUserFiles(Optional.of("remoteUserFiles"))
      .remoteConf(Optional.of("remoteConf"))
      .remoteResult(Optional.of("remoteResult"))
      .infoLog(Path.of("infoLog"))
      .debugLog(Path.of("debugLog"))
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }
}
