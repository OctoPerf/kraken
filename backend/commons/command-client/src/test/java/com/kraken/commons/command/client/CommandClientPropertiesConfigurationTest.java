package com.kraken.commons.command.client;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = {CommandClientConfiguration.class},
    initializers = {ConfigFileApplicationContextInitializer.class})
public class CommandClientPropertiesConfigurationTest {

  @Autowired
  CommandClientProperties properties;

  @Qualifier("webClientCommand")
  @Autowired
  WebClient commandWebClient;

  @Test
  public void shouldCreateProperties() {
    Assertions.assertThat(properties.getCommandUrl()).isNotNull();
  }

  @Test
  public void shouldCreateWebClients() {
    Assertions.assertThat(commandWebClient).isNotNull();
  }
}
