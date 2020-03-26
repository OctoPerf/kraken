package com.kraken.telegraf;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.NullPointerTester;
import com.kraken.analysis.properties.api.InfluxDBClientProperties;
import com.kraken.runtime.command.Command;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.google.common.testing.NullPointerTester.Visibility.PACKAGE;
import static com.kraken.tools.environment.KrakenEnvironmentKeys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommandSupplier.class, TestPropertiesConfig.class, ImmutableTelegrafProperties.class})
public class CommandSupplierTest {
  @Autowired
  CommandSupplier commandSupplier;
  @MockBean
  InfluxDBClientProperties properties;

  @Before
  public void setUp() {
    when(properties.getUrl()).thenReturn("url");
    when(properties.getDatabase()).thenReturn("database");
    when(properties.getUser()).thenReturn("user");
    when(properties.getPassword()).thenReturn("password");
  }

  @Test
  public void shouldConvert() {
    assertEquals(commandSupplier.get(),
      Command.builder()
        .path(".")
        .environment(ImmutableMap.<String, String>builder()
          .put(KRAKEN_INFLUXDB_URL, "url")
          .put(KRAKEN_INFLUXDB_DATABASE, "database")
          .put(KRAKEN_INFLUXDB_USER, "user")
          .put(KRAKEN_INFLUXDB_PASSWORD, "password")
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
