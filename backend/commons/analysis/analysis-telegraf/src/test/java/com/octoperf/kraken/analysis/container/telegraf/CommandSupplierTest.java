package com.octoperf.kraken.analysis.container.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.octoperf.kraken.config.api.ApplicationProperties;
import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import com.octoperf.kraken.config.runtime.container.api.ContainerProperties;
import com.octoperf.kraken.runtime.command.Command;
import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CommandSupplier.class,})
public class CommandSupplierTest {
  @Autowired
  CommandSupplier commandSupplier;
  @MockBean
  InfluxDBProperties influxDBProperties;
  @MockBean
  ContainerProperties containerProperties;
  @MockBean
  ApplicationProperties properties;

  @BeforeEach
  public void setUp() {
    given(properties.getData()).willReturn("/home/kraken");
    given(influxDBProperties.getUrl()).willReturn("url");
    given(influxDBProperties.getDatabase()).willReturn("database");
    given(influxDBProperties.getUser()).willReturn("user");
    given(influxDBProperties.getPassword()).willReturn("password");
    given(containerProperties.getHostId()).willReturn("hostId");
    given(containerProperties.getTaskId()).willReturn("taskId");
  }

  @Test
  public void shouldConvert() {
    Assertions.assertThat(commandSupplier.get()).isEqualTo(
        Command.builder()
            .path("/home/kraken")
            .environment(ImmutableMap.<KrakenEnvironmentKeys, String>builder()
                .put(KrakenEnvironmentKeys.KRAKEN_INFLUXDB_URL, "url")
                .put(KrakenEnvironmentKeys.KRAKEN_INFLUXDB_DATABASE, "database")
                .put(KrakenEnvironmentKeys.KRAKEN_INFLUXDB_USER, "user")
                .put(KrakenEnvironmentKeys.KRAKEN_INFLUXDB_PASSWORD, "password")
                .put(KrakenEnvironmentKeys.KRAKEN_TASK_ID, "taskId")
                .put(KrakenEnvironmentKeys.KRAKEN_HOST_ID, "hostId")
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
