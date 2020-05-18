package com.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.config.influxdb.api.InfluxDBProperties;
import com.kraken.config.runtime.container.api.ContainerProperties;
import com.kraken.runtime.command.Command;
import com.kraken.tools.environment.KrakenEnvironmentKeys;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommandSupplier.class,})
public class CommandSupplierTest {
  @Autowired
  CommandSupplier commandSupplier;
  @MockBean
  InfluxDBProperties properties;
  @MockBean
  ContainerProperties containerProperties;

  @Before
  public void setUp() {
    given(properties.getUrl()).willReturn("url");
    given(properties.getDatabase()).willReturn("database");
    given(properties.getUser()).willReturn("user");
    given(properties.getPassword()).willReturn("password");
    given(containerProperties.getHostId()).willReturn("hostId");
    given(containerProperties.getTaskId()).willReturn("taskId");
  }

  @Test
  public void shouldConvert() {
    assertThat(commandSupplier.get()).isEqualTo(
        Command.builder()
            .path(".")
            .environment(ImmutableMap.<KrakenEnvironmentKeys, String>builder()
                .put(KRAKEN_INFLUXDB_URL, "url")
                .put(KRAKEN_INFLUXDB_DATABASE, "database")
                .put(KRAKEN_INFLUXDB_USER, "user")
                .put(KRAKEN_INFLUXDB_PASSWORD, "password")
                .put(KRAKEN_TASK_ID, "taskId")
                .put(KRAKEN_HOST_ID, "hostId")
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
