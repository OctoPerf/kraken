package com.kraken.gatling.container.recorder;

import com.google.common.collect.ImmutableMap;
import com.kraken.config.gatling.api.GatlingLog;
import com.kraken.config.gatling.api.GatlingProperties;
import com.kraken.config.gatling.api.GatlingSimulation;
import com.kraken.runtime.command.Command;
import com.kraken.test.utils.TestUtils;
import com.kraken.config.api.LocalRemoteProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.collect.ImmutableList.of;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommandSupplier.class)
public class CommandSupplierTest {

  @MockBean
  GatlingProperties gatlingProperties;
  @MockBean
  GatlingLog log;
  @MockBean
  LocalRemoteProperties harPath;
  @MockBean
  GatlingSimulation simulation;

  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {
    given(gatlingProperties.getBin()).willReturn("gatlingBin");
    given(gatlingProperties.getLogs()).willReturn(log);
    given(gatlingProperties.getJavaOpts()).willReturn("-Dfoo=\"bar\"");
    given(gatlingProperties.getHarPath()).willReturn(harPath);
    given(gatlingProperties.getSimulation()).willReturn(simulation);
    given(harPath.getLocal()).willReturn("localHarPath");
    given(log.getInfo()).willReturn("infoLog");
    given(log.getDebug()).willReturn("debugLog");
    given(simulation.getPackageName()).willReturn("simulationPackage");
    given(simulation.getClassName()).willReturn("simulationClass");

    assertThat(commandSupplier.get()).isEqualTo(Command.builder()
        .path("gatlingBin")
        .environment(ImmutableMap.of(KRAKEN_GATLING_LOGS_INFO, "infoLog",
          KRAKEN_GATLING_LOGS_DEBUG, "debugLog",
            JAVA_OPTS, "-Dfoo=\"bar\""))
        .command(of(
            "./recorder.sh",
            "--headless", "true",
            "--mode", "Har",
            "--har-file", "localHarPath",
            "--package", "simulationPackage",
            "--class-name", "simulationClass"
        )).build());
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(CommandSupplier.class);
  }
}
