package com.octoperf.kraken.gatling.container.runner;

import com.google.common.collect.ImmutableMap;
import com.octoperf.kraken.config.api.LocalRemoteProperties;
import com.octoperf.kraken.config.gatling.api.GatlingLog;
import com.octoperf.kraken.config.gatling.api.GatlingProperties;
import com.octoperf.kraken.config.gatling.api.GatlingSimulation;
import com.octoperf.kraken.command.entity.Command;
import com.octoperf.kraken.tests.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.google.common.collect.ImmutableList.of;
import static com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CommandSupplier.class})
public class CommandSupplierTest {

  @MockBean
  GatlingProperties gatlingProperties;
  @MockBean
  GatlingLog log;
  @MockBean
  LocalRemoteProperties results;
  @MockBean
  GatlingSimulation simulation;

  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {

    given(gatlingProperties.getBin()).willReturn("gatlingBin");
    given(gatlingProperties.getLogs()).willReturn(log);
    given(gatlingProperties.getJavaOpts()).willReturn("-Dfoo=\"bar\"");
    given(gatlingProperties.getResults()).willReturn(results);
    given(gatlingProperties.getSimulation()).willReturn(simulation);
    given(gatlingProperties.getDescription()).willReturn("description");
    given(results.getLocal()).willReturn("localResult");
    given(log.getInfo()).willReturn("infoLog");
    given(log.getDebug()).willReturn("debugLog");
    given(simulation.getName()).willReturn("simulation");

    assertThat(commandSupplier.get()).isEqualTo(Command.builder()
        .path("gatlingBin")
        .environment(ImmutableMap.of(KRAKEN_GATLING_LOGS_INFO, "infoLog",
          KRAKEN_GATLING_LOGS_DEBUG, "debugLog",
            JAVA_OPTS, "-Dfoo=\"bar\""))
        .args(of("./gatling.sh",
            "-s",
            "simulation",
            "-rd",
            "description",
            "-rf",
            "localResult")).build());
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(CommandSupplier.class);
  }
}
