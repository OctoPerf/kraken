package com.kraken.runtime.gatling;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES;
import static com.kraken.test.utils.TestUtils.shouldPassToString;
import static org.junit.Assert.assertNotNull;

public class ImmutableGatlingExecutionPropertiesTest {

  public static final GatlingExecutionProperties GATLING_PROPERTIES = ImmutableGatlingExecutionProperties.builder()
    .container(RUNTIME_PROPERTIES)
    .home("gatlingHome")
    .bin("gatlingBin")
    .localUserFiles("localUserFiles")
    .localConf("localConf")
    .localLib("localLib")
    .localResult("localResult")
    .remoteUserFiles("remoteUserFiles")
    .remoteConf("remoteConf")
    .remoteLib("remoteLib")
    .remoteResult("remoteResult")
    .infoLog("infoLog")
    .debugLog("debugLog")
    .simulation("simulation")
    .description("description")
    .localHarPath("localHarPath")
    .remoteHarPath("remoteHarPath")
    .simulationClass("simulationClass")
    .simulationPackage("simulationPackage")
    .javaOpts("-Dfoo=\"bar\"")
    .build();

  @Test
  public void shouldPassTestUtils() {
    shouldPassToString(GATLING_PROPERTIES);
  }

  @Test
  public void shouldPassEqualsVerifier() {
    EqualsVerifier.forClass(ImmutableGatlingExecutionProperties.class).verify();
  }

  @Test
  public void shouldCreate() {
    assertNotNull(GATLING_PROPERTIES);
  }
}
