package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.influxdb.client.ImmutableInfluxDBClientProperties;
import com.kraken.runtime.command.Command;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommandSupplier.class, TestPropertiesConfig.class, ImmutableInfluxDBClientProperties.class, ImmutableTelegrafProperties.class})
public class CommandSupplierTest {
  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {
    assertEquals(commandSupplier.get(),
      Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
          .put(KRAKEN_INFLUXDB_URL, "influxDbUrl")
          .put(KRAKEN_INFLUXDB_DATABASE, "influxDbDatabase")
          .put(KRAKEN_INFLUXDB_USER, "influxDbUser")
          .put(KRAKEN_INFLUXDB_PASSWORD, "influxDbPassword")
          .put(KRAKEN_TEST_ID, "taskId")
          .put(KRAKEN_INJECTOR, "hostId")
          .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build());
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
      .testConstructors(CommandSupplier.class, PACKAGE);
  }
}
