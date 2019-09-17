package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kraken.influxdb.client.InfluxDBClientPropertiesTestConfiguration;
import com.kraken.runtime.command.Command;
import com.kraken.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CommandSupplier.class, InfluxDBClientPropertiesTestConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class CommandSupplierTest {

  @Autowired
  CommandSupplier commandSupplier;

  @Test
  public void shouldConvert() {
    assertThat(commandSupplier.get()).isEqualTo(Command.builder()
        .path(".")
        .environment(ImmutableMap.of(
            "INFLUXDB_URL", "influxdbUrl",
            "INFLUXDB_DB", "influxdbDatabase",
            "INFLUXDB_USER", "influxdbUser",
            "INFLUXDB_USER_PASSWORD", "influxdbPassword"
            )
        )
        .command(ImmutableList.of("telegraf"))
        .build());
  }

  @Test
  public void shouldPassTestUtils() {
    TestUtils.shouldPassNPE(CommandSupplier.class);
  }
}
