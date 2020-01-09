package com.kraken.runtime.gatling;

import com.kraken.test.utils.TestUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

public class GatlingExecutionPropertiesTest {

  public static final GatlingExecutionProperties GATLING_PROPERTIES = GatlingExecutionProperties.builder()
      .gatlingHome(Path.of("gatlingHome"))
      .gatlingBin(Path.of("gatlingBin"))
      .localUserFiles(Path.of("localUserFiles"))
      .localConf(Path.of("localConf"))
      .localLib(Path.of("localLib"))
      .localResult(Path.of("localResult"))
      .remoteUserFiles(Optional.of("remoteUserFiles"))
      .remoteConf(Optional.of("remoteConf"))
      .remoteLib(Optional.of("remoteLib"))
      .remoteResult(Optional.of("remoteResult"))
      .infoLog(Path.of("infoLog"))
      .debugLog(Path.of("debugLog"))
      .simulation("simulation")
      .description("description")
      .localHarPath(Path.of("localHarPath"))
      .remoteHarPath("remoteHarPath")
      .simulationClass("simulationClass")
      .simulationPackage("simulationPackage")
      .javaOpts("-Dfoo=\"bar\"")
      .build();

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassAll(GATLING_PROPERTIES);
  }
}
