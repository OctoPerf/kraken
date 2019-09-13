package com.kraken.gatling.runner;

import com.google.common.collect.ImmutableMap;
import com.kraken.runtime.command.Command;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.collect.ImmutableList.of;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CommandSupplier.class, GatlingRunnerTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class CommandSupplierTest {

  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {
    assertThat(commandSupplier.get()).isEqualTo(Command.builder()
        .path("gatlingBin")
        .environment(ImmutableMap.of("KRAKEN_GATLING_RESULT_INFO_LOG", "infoLog",
            "KRAKEN_GATLING_RESULT_DEBUG_LOG", "debugLog"))
        .command(of("./gatling.sh",
            "-s",
            "computerdatabase.BasicSimulation",
            "-rd",
            "Dev test",
            "-rf",
            "localResult")).build());
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(CommandSupplier.class);
  }
}
