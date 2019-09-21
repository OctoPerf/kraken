package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.influxdb.client.InfluxDBClientProperties;
import com.kraken.influxdb.client.InfluxDBClientPropertiesTest;
import com.kraken.influxdb.client.InfluxDBClientPropertiesTestConfiguration;
import com.kraken.runtime.command.Command;
import com.kraken.runtime.container.properties.RuntimeContainerProperties;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTest;
import com.kraken.runtime.container.properties.RuntimeContainerPropertiesTestConfiguration;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CommandSupplier.class, InfluxDBClientPropertiesTestConfiguration.class, RuntimeContainerPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class CommandSupplierTest {

  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {
    assertThat(commandSupplier.get()).isEqualTo(Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
            .put("INFLUXDB_URL", "influxdbUrl")
            .put("INFLUXDB_DB", "influxdbDatabase")
            .put("INFLUXDB_USER", "influxdbUser")
            .put("INFLUXDB_USER_PASSWORD", "influxdbPassword")
            .put("KRAKEN_TEST_ID", "taskId")
            .build()
        )
        .command(ImmutableList.of("telegraf"))
        .build());
  }

  @Test
  public void shouldPassTestUtils() {
    new NullPointerTester()
        .setDefault(RuntimeContainerProperties.class, RuntimeContainerPropertiesTest.RUNTIME_PROPERTIES)
        .setDefault(InfluxDBClientProperties.class, InfluxDBClientPropertiesTest.INFLUX_DB_CLIENT_PROPERTIES)
        .testConstructors(CommandSupplier.class, PACKAGE);
  }
}
